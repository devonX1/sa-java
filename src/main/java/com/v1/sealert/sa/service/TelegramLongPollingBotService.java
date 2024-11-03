package com.v1.sealert.sa.service;

import com.v1.sealert.sa.configuration.TextConfig;
import com.v1.sealert.sa.model.*;
import com.v1.sealert.sa.util.NotificationFormatter;
import com.v1.sealert.sa.util.Telegram;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


@Service
@Data
public class TelegramLongPollingBotService extends TelegramLongPollingBot {
    @Autowired
    private TextConfig textConfig;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    DistrictService districtService;

    private SendMessage message;
    private Long chatId;
    private String userName;
    private int messageId;

    private final String botName;
    private final String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            userName = update.getMessage().getFrom().getUserName();
            messageId = update.getMessage().getMessageId();
            String text = update.getMessage().getText();
            textHandler(text);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            //int messageId = update.getCallbackQuery().getMessage().getMessageId();
            callbackHandler(callbackData);
        }
    }

    private void textHandler(String text) {
        String t = text;
        if (TextType.START.getValue().equalsIgnoreCase(t)) {
            welcome(chatId);
        } else if (TextType.GET.getValue().equalsIgnoreCase(t)) {
            getAllNotificationAfterDate();
        } else if (TextType.INFO.getValue().equalsIgnoreCase(t)) {
            info();
        } else if (TextType.CANCEL.getValue().equalsIgnoreCase(t)) {
            deleteUser();
        } else if (t.startsWith(TextType.DISTRICT_CANCEL.getValue())) {
            deleteDistrict(t);
        } else if (t.startsWith(TextType.ADD_DISTRICT.getValue())) {
            districtHandler(t);
        } else if (TextType.GET_DISTRICTS.getValue().equalsIgnoreCase(t)) {
            System.out.println("TelegramLongPollingBotService.textHandler GET_DISTRICTS-DEBUG");
            chooseDistrict();
        }
    }

    private void callbackHandler(String callbackData) {
        String cbD = callbackData;
        if (CallbackType.YES.getValue().equalsIgnoreCase(cbD)) {
            addUser();
            chooseDistrict();
        } else if (CallbackType.NOT.getValue().equalsIgnoreCase(cbD)) {
            bye();
        }
    }

    private void districtHandler(String stringWithDisricts) {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        String districtName = stringWithDisricts.substring(13);
        System.out.println(districtName);
        if (districtName.contains(",")) {
            List<String> districtNameList = districtService.makeDistrictNameListFromString(districtName);
            for (String dName: districtNameList) {
                userService.addUserDistrict(userName, dName);
            }
            message.setText(Telegram.escapeMarkdown(textConfig.getDistrictSubscription()));
            send();
        } else {
            userService.addUserDistrict(userName, districtName.trim());
            message.setText(Telegram.escapeMarkdown(textConfig.getDistrictSubscription()));
            send();
        }
    }

    private void welcome(Long chatId) {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        message.setText(Telegram.escapeMarkdown(textConfig.getWelcome()));
        InlineKeyboardMarkup inlineKeyboardMarkup = Telegram.createOneLineKeyboardMarkup(List.of("Подписаться", "Нет"), List.of("YES", "NO"));
        message.setReplyMarkup(inlineKeyboardMarkup);
        messageId = send();
    }

    private void bye() {
        deletePrevMessage(messageId);
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        message.setText(Telegram.escapeMarkdown("Хорошего дня!"));
        send();
    }

    private void info() {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        message.setText(Telegram.escapeMarkdown(textConfig.getInfo()));
        send();
    }

    private void chooseDistrict() {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        List<District> listDistrict = districtService.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < listDistrict.size(); i++) {
            stringBuilder.append(NotificationFormatter.upperFirstLetter(listDistrict.get(i).getName()));
            if (!(i == listDistrict.size() - 1)) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("\n").append("\n");
        stringBuilder.append(textConfig.getGetDistricts());
        message.setText(Telegram.escapeMarkdown(stringBuilder.toString()));
        send();
    }

    private void deleteDistrict(String stringWithDisricts) {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        String districtName = stringWithDisricts.substring(16);
        System.out.println(districtName);
        userService.deleteUserDistrict(userName, districtName.trim());
        message.setText(Telegram.escapeMarkdown(textConfig.getDeleteDistrict()));
        send();
    }

    private void addUser() {
        deletePrevMessage(messageId);
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        if (userAlreadyAddedOrDeleted()) {
            message.setText(Telegram.escapeMarkdown("Вы были успешно добавлены."));
            send();
        } else {
            userService.addUser(userName, String.valueOf(chatId));
        }
    }

    private boolean userAlreadyAddedOrDeleted() {
        Optional<User> addedUser = userService.findByName(userName);
        return addedUser.isPresent();
    }

    private SendMessage deleteUser() {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        if (!userAlreadyAddedOrDeleted()) {
            message.setText(Telegram.escapeMarkdown(textConfig.getBye()));
            send();
        } else {
            userService.deleteUser(userName);
            message.setText(Telegram.escapeMarkdown(textConfig.getBye()));
            send();
        }
        return null;
    }

    private void getAllNotificationAfterDate() {
        User user = userService.findByName(userName).get();
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        List<District> districtList = userService.getDistrictList(user.getUserDistrict());
        List<Notification> notificationList = notificationService.findNotificationsByDistricts(districtList);
        StringBuilder stringBuilder = new StringBuilder();
        for (Notification n: notificationList) {
            String notificationString = Telegram.escapeMarkdown(NotificationFormatter.notificationFormat(n));
            stringBuilder.append(notificationString).append("\n");
        }
        String fullTextForMessage = stringBuilder.toString();
        if (fullTextForMessage.length() <= NotificationFormatter.TELEGRAM_MESSAGE_LIMIT) {
            message.setText(fullTextForMessage);
            send();
        } else {
            List<String> textParts = NotificationFormatter.splitMessage(fullTextForMessage, NotificationFormatter.TELEGRAM_MESSAGE_LIMIT);
            for (String part: textParts) {
                message.setText(fullTextForMessage);
                send();
            }
        }
    }

    private int send() {
        try {
            Message welcomeMessage = execute(message);
            return welcomeMessage.getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void deletePrevMessage(int messageId) {
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerBotCommands() {
        try {
            execute(Telegram.setCommands());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
    /**
     * Returns the token of the bot to be able to perform Telegram Api Requests
     *
     * @return Token of the bot
     */
    @Override
    public String getBotToken() {
        return token;
    }
}

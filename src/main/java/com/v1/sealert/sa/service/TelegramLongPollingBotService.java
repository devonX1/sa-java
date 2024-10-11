package com.v1.sealert.sa.service;

import com.v1.sealert.sa.configuration.TextConfig;
import com.v1.sealert.sa.model.CallbackType;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.TextType;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.util.NotificationFormatter;
import com.v1.sealert.sa.util.Telegram;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
        }
    }

    private void callbackHandler(String callbackData) {
        String cbD = callbackData;
        if (CallbackType.YES.getValue().equalsIgnoreCase(cbD)) {
            addUser();
        } else if (CallbackType.NOT.getValue().equalsIgnoreCase(cbD)) {
            bye();
        }
    }

    private void welcome(Long chatId) {
        message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        message.setText(Telegram.escapeMarkdown(textConfig.getWelcome()));
        InlineKeyboardMarkup inlineKeyboardMarkup = Telegram.createOneLineKeyboardMarkup(List.of("Да", "Нет"), List.of("YES", "NO"));
        message.setReplyMarkup(inlineKeyboardMarkup);
        messageId = send();
    }
    private void bye() {
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
            message.setText(Telegram.escapeMarkdown(textConfig.getNotification()));
            send();
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
            message.setText(Telegram.escapeMarkdown("Подписка была успешно удалена."));
            send();
        } else {
            userService.deleteUser(userName);
            message.setText(Telegram.escapeMarkdown("Подписка была успешно удалена."));
            send();
        }


        return null;
    }
    private void getAllNotificationAfterDate() {
        Map<String, Map<String, List<Notification>>> notificationMapMap = notificationService.getAllNotificationAfterDate();
        for (Map.Entry<String, Map<String, List<Notification>>> entry : notificationMapMap.entrySet()) {
            message = new SendMessage();
            message.enableMarkdownV2(true);
            message.setChatId(chatId);
            message.setText(NotificationFormatter.messageFormat(entry.getKey(), entry.getValue()));
            send();
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

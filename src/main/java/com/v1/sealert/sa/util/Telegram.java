package com.v1.sealert.sa.util;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Telegram {

    public static InlineKeyboardMarkup createKeyboardMarkup(List<String> buttonLabels, List<String> callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInKeyboard = new ArrayList<>();
        for (int i = 0; i < buttonLabels.size(); i++) {
            List<InlineKeyboardButton> buttonsInRows = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonLabels.get(i));
            button.setCallbackData(callbackData.get(i));
            buttonsInRows.add(button);
            rowsInKeyboard.add(buttonsInRows);
        }
        inlineKeyboardMarkup.setKeyboard(rowsInKeyboard);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createOneLineKeyboardMarkup(List<String> buttonLabels, List<String> callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonsInRows = new ArrayList<>();
        for (int i = 0; i < buttonLabels.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonLabels.get(i));
            button.setCallbackData(callbackData.get(i));
            buttonsInRows.add(button);
        }
        rowsInKeyboard.add(buttonsInRows);
        inlineKeyboardMarkup.setKeyboard(rowsInKeyboard);
        return inlineKeyboardMarkup;
    }
    public static String getEmojiByKey(String key) {
        return Emoji.getInstance().getEmojiMap().get(key);
    }
    public static SetMyCommands setCommands() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "Start the bot"));
        botCommandList.add(new BotCommand("/get", "Get the notifications"));
        botCommandList.add(new BotCommand("/info", "Get the information about the bot"));
        botCommandList.add(new BotCommand("/cancel", "Сancel subscription"));
        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(botCommandList);
        return setMyCommands;
    }

    //я пишу на новой клавиатуре привет ввсем


    public static String escapeMarkdown(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("`", "\\`")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("!", "\\!")
                .replace(">", "\\>")
                .replace(".", "\\.");
                //.replace("*", "\\*");
    }
}

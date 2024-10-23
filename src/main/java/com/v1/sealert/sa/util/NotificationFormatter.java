package com.v1.sealert.sa.util;

import com.v1.sealert.sa.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFormatter {

    public static final int TELEGRAM_MESSAGE_LIMIT = 4090;

    private static Map<String, String> emojiMap = Emoji.getInstance().getEmojiMap();

    public static String messageFormat(List<Notification> notificationList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Notification notification: notificationList) {
            String notificationString = notificationFormat(notification);
            stringBuilder.append(notificationString);
            stringBuilder.append("\n");
        }
        return Telegram.escapeMarkdown(stringBuilder.toString());
    }

    public static String notificationFormat(Notification notification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(emojiMap.get("red_pin")).append("*").append(upperFirstLetter(notification.getTown())).
                append("*").append("\n");
        stringBuilder.append("*").append(notification.getNotificationDate()).append("*").append("\n");
        stringBuilder.append("*Время:* ").append(notification.getNotificationPeriod());
        stringBuilder.append("\n");
        stringBuilder.append("*Улицы:* ");
        stringBuilder.append(notification.getStreet());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
    public static List<String> splitMessage(String text, int limit) {
        System.out.println("HttpService : splitMessage(): starting");
        List<String> parts = new ArrayList<>();
        int  index = 0;
        while (index < text.length()) {
            int end = Math.min(index + limit, text.length());
            parts.add(text.substring(index, end));
            index = end;
        }
        System.out.println("splitMessage(): parts list:");
        System.out.println(parts);
        return parts;
    }

    public static String upperFirstLetter(String district) {
        return district.substring(0, 1).toUpperCase() + district.substring(1);
    }
}

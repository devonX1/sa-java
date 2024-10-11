package com.v1.sealert.sa.util;

import com.v1.sealert.sa.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFormatter {
    private static Map<String, String> emojiMap = Emoji.getInstance().getEmojiMap();

    public static String messageFormat(String town, Map<String, List<Notification>> dateNotifListMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*Город:* ").append(town).append(emojiMap.get("red_pin")).append("\n");
        for (Map.Entry<String, List<Notification>> entry : dateNotifListMap.entrySet()) {
            stringBuilder.append("*Дата:* ").append(entry.getKey()).append("\n");
            for (Notification n : entry.getValue()) {
                String stringNotification = notificationFormat(n);
                stringBuilder.append(stringNotification).append("\n");
            }
        }
        return Telegram.escapeMarkdown(stringBuilder.toString());
    }

    private static String notificationFormat(Notification notification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*Время:* ").append(notification.getNotificationPeriod());
        stringBuilder.append("\n");
        stringBuilder.append("*Улицы:* ");
        stringBuilder.append(notification.getStreet());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}

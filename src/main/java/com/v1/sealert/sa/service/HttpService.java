package com.v1.sealert.sa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.sealert.sa.DTO.TelegramMessageDTO;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.util.NotificationFormatter;
import lombok.Data;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Data
@Service
public class HttpService {
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;

    private static final MediaType JSON = MediaType.get("application/json");
    private final String url;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    //Каждый день в 10 утра по часовому поясу белграда 0 0 10 * * *
    @Scheduled(cron = "0 0 7 * * *", zone = "Europe/Belgrade")
    public void sendNotifications() throws JsonProcessingException {
       Map<User, List<Notification>> userNotificationListMap = notificationService.getAllNotificationByUsers(userService.getUserWithDistricts());
       for (Map.Entry<User, List<Notification>> entry: userNotificationListMap.entrySet()) {
           String chatId = entry.getKey().getChatId();
           String notificationTextField = makeTextFromNotification(entry.getValue());
           if (notificationTextField.length() <= NotificationFormatter.TELEGRAM_MESSAGE_LIMIT) {
               TelegramMessageDTO telegramMessageDTO = prepareDTO(notificationTextField, chatId);
               String json = objectMapper.writeValueAsString(telegramMessageDTO);
               System.out.println(json);
               post(url, json);
           } else {
               List<String> textParts = NotificationFormatter.splitMessage(notificationTextField, NotificationFormatter.TELEGRAM_MESSAGE_LIMIT);
               for (String part: textParts) {
                   TelegramMessageDTO telegramMessageDTO = prepareDTO(part, chatId);
                   String json = objectMapper.writeValueAsString(telegramMessageDTO);
                   post(url, json);
               }
           }
       }
    }

    private String makeTextFromNotification(List<Notification> notificationList) {
        String fullNotificationsText = NotificationFormatter.messageFormat(notificationList);
        return fullNotificationsText;
    }

    private TelegramMessageDTO prepareDTO(String text, String chatId) {
        TelegramMessageDTO message = new TelegramMessageDTO(chatId, text, "MarkdownV2");
        return message;
    }

    private String post(String url, String json) {
        System.out.println("HttpService.post(): POST REQUEST METHOD STARTING");
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String res = response.body().string();
            //System.out.println(res);
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

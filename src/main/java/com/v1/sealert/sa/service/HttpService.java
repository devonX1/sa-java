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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Data
@Service
public class HttpService {

    private static final int TELEGRAM_MESSAGE_LIMIT = 4090;

    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;

    private static final MediaType JSON = MediaType.get("application/json");
    private final String url;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron = "0 */2 * * * *")
    public void sendNotifications() {
        System.out.println("HttpService.sendNotifications(): starting. В цикле достаем все chatId");
        for (String chatId : prepareChatIdFieldList()) {
           try {
               send(chatId);
           } catch (JsonProcessingException e) {
               throw new RuntimeException(e);
           }
        }

    }
    private void send(String chatId) throws JsonProcessingException {
        System.out.println("HttpService : send(): starting");
        for (String text : prepareTextFieldList()) {
            if (text.length() <= TELEGRAM_MESSAGE_LIMIT) {
                System.out.println("send(): single part text field");
                TelegramMessageDTO telegramMessageDTO = prepareDTO(text, chatId);
                String json = objectMapper.writeValueAsString(telegramMessageDTO);
                post(url, json);
            } else {
                System.out.println("send(): multiparts text field");
                List<String> textParts = splitMessage(text, TELEGRAM_MESSAGE_LIMIT);
                for (String part: textParts) {
                    TelegramMessageDTO telegramMessageDTO = prepareDTO(part, chatId);
                    String json = objectMapper.writeValueAsString(telegramMessageDTO);
                    post(url, json);
                }
            }
        }
    }
    private TelegramMessageDTO prepareDTO(String text, String chatId) {
        TelegramMessageDTO message = new TelegramMessageDTO(chatId, text, "MarkdownV2");
        return message;
    }
    private List<String> prepareChatIdFieldList() {
        List<String> chatIdList = new ArrayList<>();
        for (User u : userService.getAllUsers()) {
            chatIdList.add(u.getChatId());
        }
        System.out.println("HttpService.prepareChatIdFieldList():");
        System.out.println(chatIdList);
        return chatIdList;
    }
    private List<String> prepareTextFieldList() {
        System.out.println("HttpService.prepareTextFieldList(): starting");
        Map<String, Map<String, List<Notification>>> notificationMapMap = notificationService.getAllNotificationAfterDate();
        List<String> textFieldList = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Notification>>> entry : notificationMapMap.entrySet()) {
            String text = prepareTextField(entry);
            textFieldList.add(text);
        }
        return textFieldList;
    }
    private String prepareTextField(Map.Entry<String, Map<String, List<Notification>>> text) {
        return NotificationFormatter.messageFormat(text.getKey(), text.getValue());
    }
    private String post(String url, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private List<String> splitMessage(String text, int limit) {
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
}

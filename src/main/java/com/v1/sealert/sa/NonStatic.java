package com.v1.sealert.sa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.sealert.sa.DTO.TelegramMessageDTO;
import com.v1.sealert.sa.configuration.JdbcConfig;
import com.v1.sealert.sa.configuration.TextConfig;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.service.NotificationService;
import com.v1.sealert.sa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NonStatic {

    @Autowired
    NotificationService notificationService;
    @Autowired
    TextConfig textConfig;



    public void tt() {
        String messageText = "*Город*: Темерин📍\n" +
                "*Дата*: 2024\\-08\\-30\n" +
                "*Время*: 08\\:30 \\- 11\\:00\n" +
                "*Улицы*: Сириг\\: Косовска од Његошеве до Београдске, И\\.Л\\.Рибара";
        TelegramMessageDTO message = new TelegramMessageDTO("131504666", messageText, "MarkdownV2");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(message);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

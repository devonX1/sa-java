package com.v1.sealert.sa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String chatId;
    private LocalDateTime createAt;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChatId() {
        return chatId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chatId='" + chatId + '\'' +
                ", createAt=" + createAt +
                '}';
    }
    public User(String name, String chatId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.chatId = chatId;
        this.createAt = LocalDateTime.now();
    }
}

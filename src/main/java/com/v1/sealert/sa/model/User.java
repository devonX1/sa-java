package com.v1.sealert.sa.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID guid;
    private String name;
    private String chatId;
    private LocalDateTime dateCreate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + guid +
                ", name='" + name + '\'' +
                ", chatId='" + chatId + '\'' +
                ", createAt=" + dateCreate +
                '}';
    }
    public User(String name, String chatId) {
        this.guid = UUID.randomUUID();
        this.name = name;
        this.chatId = chatId;
        this.dateCreate = LocalDateTime.now();
    }
}

package com.v1.sealert.sa.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserDistrict> userDistrict = new ArrayList<>();

    public User(String name, String chatId) {
        this.name = name;
        this.chatId = chatId;
        this.dateCreate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + guid +
                ", name='" + name + '\'' +
                ", chatId='" + chatId + '\'' +
                ", createAt=" + dateCreate +
                '}';
    }
}

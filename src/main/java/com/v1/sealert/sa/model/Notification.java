package com.v1.sealert.sa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID guid;
    private String branch;
    private String town;
    private String street;
    private String notificationPeriod;
    private LocalDate notificationDate;
    private LocalDateTime dateCreate;

    public Notification(String branch, String town, String street, String notificationPeriod, String notificationDate) {
        this.branch = branch;
        this.town = town;
        this.street = street;
        this.notificationPeriod = notificationPeriod;
        this.notificationDate = stringToLocalDateTime(notificationDate);
        this.dateCreate = LocalDateTime.now();
    }

    private LocalDate stringToLocalDateTime(String notificationDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(notificationDate, dateTimeFormatter);

    }
}

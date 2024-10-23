package com.v1.sealert.sa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DateDTO {
    @JsonProperty("notificationDate")
    private String notificationDate;

    @JsonProperty("notifications")
    private List<NotificationDTO> notifications;
}

package com.v1.sealert.sa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TelegramMessageDTO {
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("text")
    private String text;
    @JsonProperty("parse_mode")
    private String parseMode;
}

package com.v1.sealert.sa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationDTO {
    @JsonProperty("branch")
    private String branch;
    @JsonProperty("town")
    private String town;
    @JsonProperty("time")
    private String time;
    @JsonProperty("street")
    private String street;
}

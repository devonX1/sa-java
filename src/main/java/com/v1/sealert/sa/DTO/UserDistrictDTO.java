package com.v1.sealert.sa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDistrictDTO {
    private String chatId;
    private String userName;
    private String districtName;
}

package com.v1.sealert.sa.controller;

import com.v1.sealert.sa.DTO.DateDTO;
import com.v1.sealert.sa.DTO.NotificationDTO;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.service.DistrictService;
import com.v1.sealert.sa.service.HttpService;
import com.v1.sealert.sa.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DistrictService districtService;

    @PostMapping
    public ResponseEntity<Void> createNotifications(@RequestBody DateDTO dateDTO) {
        List<NotificationDTO> notificationDTOList = dateDTO.getNotifications();
        List<Notification> notificationList = notificationService.addAllNotification(notificationDTOList, dateDTO.getNotificationDate());
        districtService.addAllDistricts(notificationList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

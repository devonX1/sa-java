package com.v1.sealert.sa.service;

import com.v1.sealert.sa.DTO.NotificationDTO;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    private final LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Belgrade"));

    public List<Notification> getAllNotification() {
        return (List<Notification>) notificationRepository.findAll();
    }
    public Notification addNotification(String branch, String town, String street, String description, String notificationTime) {
        Notification notification = new Notification(branch, town, street, description, notificationTime);
        return notificationRepository.save(notification);
    }
    public Map<String, Map<String, List<Notification>>> getAllNotificationAfterDate() {
        System.out.println("NotificationService.getAllNotificationAfterDate(): starting");
        List<Notification> notificationList = notificationRepository.getAllNotificationAfterDate(localDate);
        Map<String, List <Notification>> notificationMapList = notificationListToMapList(notificationList);
        Map<String, Map<String, List<Notification>>> notificationMapMap = notificationListToMapMap(notificationMapList);
        return notificationMapMap;
    }
    public List<Notification> addAllNotification(List<NotificationDTO> notificationDTOList, String notificationDate) {
        List<Notification> notificationList = notificationsDTOtoNotification(notificationDTOList, notificationDate);
        return  (List<Notification>) notificationRepository.saveAll(notificationList);
    }

    private List<Notification> notificationsDTOtoNotification(List<NotificationDTO> notificationDTOList, String notificationDate) {
        String nStringDate = makeDate(notificationDate);
        List<Notification> notificationList = new ArrayList<>();
        for (NotificationDTO notificationDTO : notificationDTOList) {
            notificationList.add(new Notification(notificationDTO.getBranch(), notificationDTO.getTown(), notificationDTO.getStreet(), notificationDTO.getTime(), nStringDate));
        }
        return notificationList;
    }

    private String makeDate(String notificationDate) {
        DateTimeFormatter dateFormatterInput = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dateFormatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nDate = LocalDate.parse(notificationDate, dateFormatterInput);
        String nStringDate = nDate.format(dateFormatterOutput);
        return nStringDate;
    }
    //Из списка уведомлений создаем карту, где ключем является город, а значением список уведомлений для этого города
    public Map<String, List <Notification>> notificationListToMapList(List<Notification> notificationList) {
        System.out.println("NotificationService.notificationListToMapList(): starting");
        Map<String, List<Notification>> notificationMapList = new HashMap<>();
        for (Notification n: notificationList) {
            String town = n.getTown();
            if (notificationMapList.containsKey(town)) {
                notificationMapList.get(town).add(n);
            } else {
                List<Notification> notifications = new ArrayList<>();
                notifications.add(n);
                notificationMapList.put(town, notifications);
            }
        }
        //System.out.println("NotificationService.notificationListToMapList() maps:");
        //System.out.println(notificationMapList);
        return notificationMapList;
    }
    //Из карты, где ключем является город, а значением список уведомлений, мы создаем карту, где ключем также остается город,
    // но значением становится карта, у которой в свою очередь ключ - дата, а значение список уведомлений
    public Map<String, Map<String, List<Notification>>> notificationListToMapMap(Map<String, List <Notification>> notificationMapList) {
        Map<String, Map<String, List<Notification>>> notificationMapMap = new HashMap<>();
        for (Map.Entry<String, List<Notification>> entry : notificationMapList.entrySet()) {
                Map<String, List <Notification>> notificationsMap = notificationListToMapList2(entry.getValue());
                notificationMapMap.put(entry.getKey(), notificationsMap);
        }
        return notificationMapMap;
    }
    //Из списка уведомлений создаем карту, где ключем является дата, а значением уведомления по этой дате
    public Map<String, List <Notification>> notificationListToMapList2(List<Notification> notificationList) {
        Map<String, List<Notification>> notificationMapList2 = new HashMap<>();
        for (Notification n: notificationList) {
            String date = String.valueOf(n.getNotificationDate());
            if (notificationMapList2.containsKey(date)) {
                notificationMapList2.get(date).add(n);
            } else {
                List<Notification> notifications = new ArrayList<>();
                notifications.add(n);
                notificationMapList2.put(date, notifications);
            }
        }
        return notificationMapList2;
    }
}

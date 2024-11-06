package com.v1.sealert.sa.service;

import com.v1.sealert.sa.DTO.NotificationDTO;
import com.v1.sealert.sa.model.District;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.repo.NotificationRepository;
import com.v1.sealert.sa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    //Из карты с юзерами и дистриктами делаем карту с Юзерами и нотификациями по их дистриктам
    @Transactional
    public Map<User, List<Notification>> getAllNotificationByUsers(Map<User, List<District>> userDistrictsMap) {
        Map<User, List<Notification>> userNotificationListMap = new HashMap<>();
        for (Map.Entry<User, List<District>> entry: userDistrictsMap.entrySet()) {
            List<Notification> notificationList = findNotificationsByDistricts(entry.getValue());
            userNotificationListMap.put(entry.getKey(), notificationList);
        }
        //System.out.println("NotificationRepository.getAllNotificationByUsers(): user->notif maps below:");
        //System.out.println(userNotificationListMap);
        return userNotificationListMap;
    }
    //Из списка дистриктов возвращаем список нотификаций
    public List<Notification> findNotificationsByDistricts(List<District> districtList) {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Belgrade"));
        List<Notification> notificationList = new ArrayList<>();
        for (District d: districtList) {
            notificationList.addAll(notificationRepository.findByTownAndNotificationDateGreaterThanEqual(d.getName(), localDate));
        }
        //System.out.println("NotificationService.findNotificationsByDistricts*(): Notification list below: ");
        //System.out.println("List SIZE ======: " + notificationList.size());
        //System.out.println(notificationList);
        return notificationList;
    }

    public List<Notification> getAllNotification() {
        return (List<Notification>) notificationRepository.findAll();
    }
    @Transactional
    public Notification addNotification(String branch, String town, String street, String description, LocalDate notificationTime) {
        Notification notification = new Notification(branch, town, street, description, notificationTime);
        return notificationRepository.save(notification);
    }

    public List<Notification> addAllNotification(List<NotificationDTO> notificationDTOList, String notificationDate) {
        List<Notification> notificationList = notificationsDTOtoNotification(notificationDTOList, notificationDate);
        return  (List<Notification>) notificationRepository.saveAll(notificationList);
    }

    public boolean isPresent(String street, String time, LocalDate ldate) {
        Optional<Notification> notificationOptional = notificationRepository.findByStreetAndNotificationPeriodAndNotificationDate(street, time, ldate);
        return notificationOptional.isPresent();
    }

    private List<Notification> notificationsDTOtoNotification(List<NotificationDTO> notificationDTOList, String notificationDate) {
        LocalDate nDate = makeDateV2(notificationDate);
        List<Notification> notificationList = new ArrayList<>();
        for (NotificationDTO notificationDTO : notificationDTOList) {
            String street = notificationDTO.getStreet();
            String time = notificationDTO.getTime();
            if (isPresent(street, time, nDate)) {
                System.out.println("NotificationService.notificationsDTOtoNotification(): Notification with street: >>" + street + " has already been added to the database");
                continue;
            }
            notificationList.add(new Notification(notificationDTO.getBranch(), notificationDTO.getTown().toLowerCase(), notificationDTO.getStreet(), notificationDTO.getTime(), nDate));
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

    private LocalDate makeDateV2(String notificationDate) {
        DateTimeFormatter dateFormatterInput = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dateFormatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nDate = LocalDate.parse(notificationDate, dateFormatterInput);
        String nStringDate = nDate.format(dateFormatterOutput);
        return LocalDate.parse(nStringDate, dateFormatterOutput);
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

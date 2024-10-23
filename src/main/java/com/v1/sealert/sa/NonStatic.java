package com.v1.sealert.sa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.sealert.sa.DTO.TelegramMessageDTO;
import com.v1.sealert.sa.configuration.JdbcConfig;
import com.v1.sealert.sa.configuration.TextConfig;
import com.v1.sealert.sa.model.District;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.model.UserDistrict;
import com.v1.sealert.sa.repo.DistrictRepository;
import com.v1.sealert.sa.repo.NotificationRepository;
import com.v1.sealert.sa.repo.UserDistrictRepository;
import com.v1.sealert.sa.repo.UserRepository;
import com.v1.sealert.sa.service.HttpService;
import com.v1.sealert.sa.service.NotificationService;
import com.v1.sealert.sa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class NonStatic {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    UserDistrictRepository userDistrictRepository;
    @Autowired
    UserService userService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    HttpService httpService;

    void manyToManyTest() {
        District d1 = new District("district_one");
        District d2 = new District("district_two");
        addDistrict(d1, d2);

        User u = userRepository.findById(UUID.fromString("b9f8ddf7-2dae-46f5-a1d9-bfb0e9ad31de")).get();

        d1 = districtRepository.findByName("district_one").get();

        UserDistrict userDistrict = new UserDistrict(u, d1);
        userDistrictRepository.save(userDistrict);
    }

    private void addDistrict(District d1, District d2) {
        districtRepository.saveAll(List.of(d1, d2));
    }

    void cascadeDeleteTest() {
        userRepository.deleteById(UUID.fromString("be40a037-fc93-4991-8e58-9912f0402b7d"));
    }
    void addUserDistrictTest() {
        userService.addUserDistrict("AbyssInsideInYou", "Стара Пазова");
    }
    void findByTownAndNotificationDateTest() {
        List<Notification> notifications = notificationRepository.findByTownAndNotificationDateGreaterThanEqual("суботица", LocalDate.now());
        System.out.println(notifications);
        System.out.println(notifications.size());
    }
    //@Transactional
    void findAllWithDistrictsTest() {
        List<User> userList = userService.findAllWithDistricts();
        System.out.println(userList);
        for (User u: userList) {
            List<UserDistrict> userDistrictSet = u.getUserDistrict();
            userDistrictSet.size(); // это вызовет загрузку данных, если они были лениво загружены
            System.out.println("Size of set: " + userDistrictSet.size());
            System.out.println(userDistrictSet);
        }
    }

    void findAllTest() {
        List<User> userList = (List<User>) userRepository.findAll();
        System.out.println(userList);
        for (User u: userList) {
            List<UserDistrict> userDistrictSet = u.getUserDistrict();
            userDistrictSet.size(); // это вызовет загрузку данных, если они были лениво загружены
            System.out.println("Size of set: " + userDistrictSet.size());
            System.out.println(userDistrictSet);
        }
    }
    void getUserWithNotificationTest() {
        Map<User, List<Notification>> unomap = notificationService.getAllNotificationByUsers(userService.getUserWithDistricts());
        for (Map.Entry<User, List<Notification>> entry: unomap.entrySet()) {
            //System.out.println(httpService.makeTextFromNotification(entry.getValue()));
            //System.out.println("-----------USER: " + entry.getKey());
            //System.out.println("_");
            //System.out.println("_");
            //System.out.println("_");
            //System.out.println(entry.getValue());
            //System.out.println(entry.getValue().size());;
        }
    }
    void getOneUserDistrictTest() {
        User u = userService.findByName("AbyssInsideInYou").get();
        System.out.println(u.getUserDistrict());
    }
    void userDistrictDeleteTest() {
        userDistrictRepository.deleteUserDistrictByUserAndDistrict(UUID.fromString("0c546354-21bb-4ba9-8736-cc02f6d3a977"),
                UUID.fromString("bbf24aa5-66a2-493e-828d-dd5bcc6cdc5c"));
    }
}

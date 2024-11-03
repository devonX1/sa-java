package com.v1.sealert.sa.service;

import com.v1.sealert.sa.model.District;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.model.UserDistrict;
import com.v1.sealert.sa.repo.DistrictRepository;
import com.v1.sealert.sa.repo.UserDistrictRepository;
import com.v1.sealert.sa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private UserDistrictRepository userDistrictRepository;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    @Transactional
    public User addUser(String name, String chatId) {
        User u = new User(name, chatId);
        return userRepository.save(u);
    }
    @Transactional
    public void addUserDistrict(String name, String district) {
        User u = this.findByName(name).get();
        District d = districtRepository.findByName(district.toLowerCase().trim()).get();
        UserDistrict userDistrict = new UserDistrict(u, d);
        userDistrictRepository.save(userDistrict);
    }
    @Transactional
    public void deleteUserDistrict(String name, String district) {
        userDistrictRepository.deleteUserDistrictByUserAndDistrict(this.findByName(name).get().getGuid(),
                districtRepository.findByName(district.toLowerCase()).get().getGuid());
    }
    @Transactional
    public void deleteUser(String name) {
        userRepository.deleteById(userRepository.findByName(name).get().getGuid());
    }

    public Optional<User> findByName(String name) {
        Optional<User> r = userRepository.findByName(name);
        return r;
    }
    //Сначала делаем карту юзеров и привязанных к ним дистриктов
    public Map<User, List<District>> getUserWithDistricts() {
        Map<User, List<District>> userDistrictsMap = new HashMap<>();
        List<User> userList = this.findAllWithDistricts();
        for (User u: userList) {
            List<District> districtList = getDistrictList(u.getUserDistrict());
            //System.out.println("UserService.getUserWithDistricts():");
            //System.out.println(districtList);
            userDistrictsMap.put(u, districtList);
        }
        System.out.println("UserService.getUserWithDistricts(): UserDistrictsMap below:");
        System.out.println(userDistrictsMap);
        return userDistrictsMap;
    }
    //Возвращаем список пользователей с заполненными связями Юзер_дистрикт
    //@Transactional(readOnly = true)
    public List<User> findAllWithDistricts() {
        return userRepository.findAllWithDistricts();
    }
    //Из связей по юзеру возвращаем плоский список дистриктов
    public List<District> getDistrictList(List<UserDistrict> userDistrictList) {
        List<District> districtList = new ArrayList<>();
        for (UserDistrict userDistrict: userDistrictList) {
            districtList.add(userDistrict.getDistrict());
        }
        return districtList;
    }
}

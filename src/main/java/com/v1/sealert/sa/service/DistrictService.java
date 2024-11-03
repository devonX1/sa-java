package com.v1.sealert.sa.service;

import com.v1.sealert.sa.model.District;
import com.v1.sealert.sa.model.Notification;
import com.v1.sealert.sa.model.UserDistrict;
import com.v1.sealert.sa.repo.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    public Optional<District> findByName(String name) {
        Optional<District> d = districtRepository.findByName(name.toLowerCase());
        return d;
    }

    @Transactional
    public void addAllDistricts(List<Notification> notificationList) {
        Set<String> DistinctDistrictName = new HashSet<>();
        for (Notification n : notificationList) {
            String name = n.getTown().toLowerCase();
            if (isPresent(name)) {
                System.out.println("DistrictService.addAllDistricts(): " + name + " already added to db");
                continue;
            }
            DistinctDistrictName.add(name);
        }
        System.out.println("DistrictService.addAllDistricts(): debug set of districts");
        System.out.println(DistinctDistrictName);
        districtRepository.saveAll(createDistinctDistrictList(DistinctDistrictName));
    }

    public boolean isPresent(String name) {
        Optional<District> districtOptional = districtRepository.findByName(name);
       return districtOptional.isPresent();
    }

    public List<District> findAll() {
       return districtRepository.findAll();
    }

    public List<String> makeDistrictNameListFromString(String text) {
        List<String> districtNameList = new ArrayList<>();
        String[] distirctArray = text.split(",");
        System.out.println(distirctArray);
        for (int i = 0; i < distirctArray.length; i++) {
            districtNameList.add(distirctArray[i].trim());
        }
        return districtNameList;
    }

    private List<District> createDistinctDistrictList(Set<String> DistinctDistrictName) {
        List<District> districtList = new ArrayList<>();
        for (String name: DistinctDistrictName) {
            districtList.add(new District(name));
        }
        return districtList;
    }
}

package com.v1.sealert.sa.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class TextConfig {
    @Value("${spring.text.welcome}")
    private String welcome;
    @Value("${spring.text.notification}")
    private String notification;
    @Value("${spring.text.info}")
    private String info;
    @Value("${spring.text.district-subscription}")
    private String districtSubscription;
    @Value("${spring.text.get-district}")
    private String getDistricts;
    @Value("${spring.text.delete-district}")
    private String deleteDistrict;
    @Value("${spring.text.bye}")
    private String bye;
}

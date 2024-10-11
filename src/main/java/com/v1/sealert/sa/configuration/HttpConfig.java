package com.v1.sealert.sa.configuration;

import com.v1.sealert.sa.service.HttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Bean
    public HttpService httpService(@Value("${spring.telegram.url}") String url) {
        return new HttpService(url);
    }
}

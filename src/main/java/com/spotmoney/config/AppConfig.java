package com.spotmoney.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Bean
 */
@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

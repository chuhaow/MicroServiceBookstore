package com.cwen.cart_service.catalog;


import com.cwen.cart_service.ApplicationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class CatalogServiceConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ApplicationProperties applicationProperties) {
        System.out.println("Inside restTemplate: "+applicationProperties.catalogServiceUrl());
        return restTemplateBuilder
                .rootUri(applicationProperties.catalogServiceUrl())
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(5))
                .build();
    }
}

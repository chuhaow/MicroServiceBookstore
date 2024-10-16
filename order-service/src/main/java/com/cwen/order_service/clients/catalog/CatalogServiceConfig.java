package com.cwen.order_service.clients.catalog;

import com.cwen.order_service.ApplicationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class CatalogServiceConfig {
    @Bean
    RestClient restClient(ApplicationProperties applicationProperties) {
        return RestClient.builder()
                .baseUrl(applicationProperties.catalogServiceUrl())
                .requestFactory(ClientHttpRequestFactories
                        .get(ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(5)) //Timeout for establishing connection
                                .withReadTimeout(Duration.ofSeconds(5)))) //Timeout for receiving response
                .build();
    }
}

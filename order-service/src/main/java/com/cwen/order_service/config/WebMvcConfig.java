package com.cwen.order_service.config;

import com.cwen.order_service.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${orders.cors.path-pattern}")
    private String pathPattern;

    @Value("${orders.cors.allowed-origin-patterns}")
    private String allowedOriginPatterns;

    @Value("${orders.cors.allowed-methods}")
    private String[] allowedMethods;

    @Value("${orders.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${orders.cors.allow-credentials}")
    private boolean allowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(pathPattern)
                .allowedOriginPatterns(allowedOriginPatterns)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials);
    }

}

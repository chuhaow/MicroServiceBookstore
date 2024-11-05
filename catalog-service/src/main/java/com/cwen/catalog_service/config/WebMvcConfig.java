package com.cwen.catalog_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${catalog.cors.path-pattern}")
    private String pathPattern;

    @Value("${catalog.cors.allowed-origin-patterns}")
    private String allowedOriginPatterns;

    @Value("${catalog.cors.allowed-methods}")
    private String[] allowedMethods;

    @Value("${catalog.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${catalog.cors.allow-credentials}")
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

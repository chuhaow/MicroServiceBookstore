package com.cwen.cart_service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cart")
public record ApplicationProperties(
        String catalogServiceUrl
) { }

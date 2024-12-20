package com.cwen.cart_service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cart")
public class ApplicationProperties {
    private String catalogServiceUrl;


    public String getCatalogServiceUrl() {
        return catalogServiceUrl;
    }


    public void setCatalogServiceUrl(String catalogServiceUrl) {
        this.catalogServiceUrl = catalogServiceUrl;
    }
}
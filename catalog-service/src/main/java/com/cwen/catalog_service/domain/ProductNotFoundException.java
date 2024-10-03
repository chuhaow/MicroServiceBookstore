package com.cwen.catalog_service.domain;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException create(String message) {
        return new ProductNotFoundException(message);
    }
}

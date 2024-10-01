package com.cwen.catalog_service.domain;

import java.math.BigDecimal;

public record Product(String code, String name, String description, String imageURL, BigDecimal price) {
}
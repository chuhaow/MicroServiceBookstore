package com.cwen.bookstore_webapp.client.catalog.models;

import java.math.BigDecimal;

public record Product(Long id, String code, String name, String description, String imageURL, BigDecimal price) {
}

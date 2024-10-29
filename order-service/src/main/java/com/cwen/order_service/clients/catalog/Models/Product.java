package com.cwen.order_service.clients.catalog.Models;

import java.math.BigDecimal;

public record Product(String code, String name, String description, String imageURL, BigDecimal price) {
}

package com.cwen.cart_service.domain.models;

import java.math.BigDecimal;

public record CartItemDTO(String code, String name, BigDecimal price, int quantity) {}
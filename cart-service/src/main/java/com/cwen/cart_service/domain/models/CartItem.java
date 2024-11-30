package com.cwen.cart_service.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CartItem(
        @NotBlank(message = "Code is required") String code,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Price is required") BigDecimal price,
        @NotNull @Min(0) Integer quantity) { }

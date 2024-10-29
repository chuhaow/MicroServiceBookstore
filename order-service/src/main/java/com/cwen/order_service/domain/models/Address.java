package com.cwen.order_service.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "Must have a primary address") String addressLine1,
        String addressLine2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "ZipCode is required") String zipCode,
        @NotBlank(message = "Country is required") String country
) { }

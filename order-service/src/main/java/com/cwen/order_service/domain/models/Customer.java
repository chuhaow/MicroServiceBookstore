package com.cwen.order_service.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record Customer(
        @NotBlank(message = "Customer Name is required") String name,
        @NotBlank(message = "Customer email is required") @Email String email,
        @NotBlank(message = "Customer Phone number is required") String phone
) { }

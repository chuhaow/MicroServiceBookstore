package com.cwen.cart_service.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuestUpdateQuantityRequest(@NotNull String guestId,
                                         @NotBlank String itemCode,
                                         @NotNull @Min(0) Integer quantity) {
}

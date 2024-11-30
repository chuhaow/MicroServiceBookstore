package com.cwen.cart_service.domain.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateItemQuantityRequest(@NotNull String userId,
                                        @Valid CartItem item) {
}

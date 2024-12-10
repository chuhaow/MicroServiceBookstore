package com.cwen.bookstore_webapp.client.cart.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateItemQuantityRequest(@NotBlank String itemCode,
                                        @NotNull @Min(0) Integer quantity) {
}

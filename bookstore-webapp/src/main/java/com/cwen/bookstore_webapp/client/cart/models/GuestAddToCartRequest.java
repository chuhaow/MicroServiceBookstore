package com.cwen.bookstore_webapp.client.cart.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GuestAddToCartRequest(@NotNull String guestId, @Valid CartItem item) {
}

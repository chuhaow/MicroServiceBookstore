package com.cwen.bookstore_webapp.client.cart;

import com.cwen.bookstore_webapp.client.cart.models.AddToCartRequest;
import com.cwen.bookstore_webapp.client.cart.models.AddedToCartResponseDTO;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.service.annotation.PostExchange;


public interface CartServiceClient {
    @PostExchange("/cart/api/carts")
    AddedToCartResponseDTO addToCart(
            @RequestBody AddToCartRequest addToCartRequest);
}

package com.cwen.bookstore_webapp.client.cart;

import com.cwen.bookstore_webapp.client.cart.models.*;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;


public interface CartServiceClient {
    @PostExchange("/cart/api/carts")
    AddedToCartResponseDTO addToCart(
            @RequestBody AddToCartRequest addToCartRequest);

    @PostExchange("/cart/api/carts/update/quantity")
    UpdateItemQuantityResponseDTO updateItemQuantity(@RequestBody UpdateItemQuantityRequest updateItemQuantityRequest);

    @GetExchange("/cart/api/carts")
    List<CartItem> getCartItems();
}

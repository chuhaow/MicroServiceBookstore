package com.cwen.bookstore_webapp.client.cart;

import com.cwen.bookstore_webapp.client.cart.models.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.List;
import java.util.Map;


public interface CartServiceClient {
    @PostExchange("/cart/api/carts")
    AddedToCartResponseDTO addToCart(
            @RequestHeader Map<String, ?> headers, @RequestBody AddToCartRequest addToCartRequest);

    @PostExchange("/cart/api/carts/update/quantity")
    UpdateItemQuantityResponseDTO updateItemQuantity(
            @RequestHeader Map<String, ?> headers, @RequestBody UpdateItemQuantityRequest updateItemQuantityRequest);

    @GetExchange("/cart/api/carts")
    List<CartItem> getCartItems(@RequestHeader Map<String, ?> headers);

    @DeleteExchange("/cart/api/carts")
    ResponseEntity<String> deleteCart(@RequestHeader Map<String, ?> headers);

}

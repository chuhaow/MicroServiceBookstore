package com.cwen.bookstore_webapp.client.cart;

import com.cwen.bookstore_webapp.client.cart.models.*;

import com.cwen.bookstore_webapp.client.catalog.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostExchange("/cart/api/carts/guest")
    AddedToCartResponseDTO guestAddToCart(
             @RequestBody GuestAddToCartRequest guestAddToCartRequest);

    @GetExchange("/cart/api/carts/guest/{guestId}")
    List<CartItem> guestGetCart(@PathVariable String guestId);

    @PostExchange("/cart/api/carts/guest/update/quantity")
    UpdateItemQuantityResponseDTO guestUpdateItemQuantity(
             @RequestBody GuestUpdateQuantityRequest guestUpdateQuantityRequest);

    @PostExchange("/cart/api/carts/merge/{guestId}")
    List<CartItem> mergeCarts(@RequestHeader Map<String, ?> headers, @PathVariable String guestId);


}

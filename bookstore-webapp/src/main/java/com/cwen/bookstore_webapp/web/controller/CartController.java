package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.cart.CartServiceClient;
import com.cwen.bookstore_webapp.client.cart.models.*;
import com.cwen.bookstore_webapp.services.SecurityHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
public class CartController {
    private final CartServiceClient cartService;
    private final SecurityHelper securityHelper;

    public CartController(final CartServiceClient cartService, SecurityHelper securityHelper) {
        this.cartService = cartService;
        this.securityHelper = securityHelper;
    }

    @PostMapping("/api/carts")
    @ResponseBody
    AddedToCartResponseDTO addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return cartService.addToCart(headers, addToCartRequest);
    }

    @PostMapping("/api/carts/update/quantity")
    @ResponseBody
    UpdateItemQuantityResponseDTO removeFromCart(@Valid @RequestBody UpdateItemQuantityRequest updateItemQuantityRequest) {
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return cartService.updateItemQuantity(headers, updateItemQuantityRequest);
    }

    @GetMapping("/api/carts")
    @ResponseBody
    List<CartItem> getCartItems(){
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return cartService.getCartItems(headers);
    }

    @DeleteMapping("/api/carts")
    @ResponseBody
    ResponseEntity<String> deleteCart(){
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return cartService.deleteCart(headers);
    }
}

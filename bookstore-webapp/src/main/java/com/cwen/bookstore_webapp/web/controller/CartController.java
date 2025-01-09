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

    @PostMapping("api/carts/guest")
    @ResponseBody
    AddedToCartResponseDTO guestAddToCart(@Valid @RequestBody GuestAddToCartRequest guestAddToCartRequest) {
        System.out.println(guestAddToCartRequest.guestId());
        return cartService.guestAddToCart(guestAddToCartRequest);
    }

    @PostMapping("/api/carts/guest/update/quantity")
    @ResponseBody
    UpdateItemQuantityResponseDTO guestUpdateQuantity(@Valid @RequestBody GuestUpdateQuantityRequest guestUpdateQuantityRequest) {
        return cartService.guestUpdateItemQuantity(guestUpdateQuantityRequest);
    }

    @GetMapping("/api/carts/guest/{guestId}")
    @ResponseBody
    List<CartItem> guestGetCart(@PathVariable String guestId) {
        return cartService.guestGetCart(guestId);
    }

    @PostMapping("/api/carts/merge/{guestId}")
    @ResponseBody
    List<CartItem> mergeCarts(@PathVariable String guestId) {
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return cartService.mergeCarts(headers,guestId);
    }
}

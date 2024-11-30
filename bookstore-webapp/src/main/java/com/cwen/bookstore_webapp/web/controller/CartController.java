package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.cart.CartServiceClient;
import com.cwen.bookstore_webapp.client.cart.models.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class CartController {
    private final CartServiceClient cartService;

    public CartController(final CartServiceClient cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/api/carts")
    @ResponseBody
    AddedToCartResponseDTO addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        System.out.println(addToCartRequest);
        return cartService.addToCart(addToCartRequest);
    }

    @PostMapping("/api/carts/update/quantity")
    @ResponseBody
    UpdateItemQuantityResponseDTO removeFromCart(@Valid @RequestBody UpdateItemQuantityRequest updateItemQuantityRequest) {
        return cartService.updateItemQuantity(updateItemQuantityRequest);
    }

    @GetMapping("/api/carts")
    @ResponseBody
    List<CartItem> getCartItems(){
        return cartService.getCartItems();
    }
}

package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.cart.CartServiceClient;
import com.cwen.bookstore_webapp.client.cart.models.AddToCartRequest;
import com.cwen.bookstore_webapp.client.cart.models.AddedToCartResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


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
}

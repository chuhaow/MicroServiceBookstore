package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.cart.CartServiceClient;
import com.cwen.bookstore_webapp.client.cart.models.AddToCartRequest;
import com.cwen.bookstore_webapp.client.cart.models.AddedToCartResponseDTO;
import com.cwen.bookstore_webapp.client.cart.models.CartItem;
import com.cwen.bookstore_webapp.client.catalog.models.PagedResult;
import com.cwen.bookstore_webapp.client.catalog.models.Product;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/api/carts")
    @ResponseBody
    List<CartItem> getCartItems(){
        return cartService.getCartItems();
    }
}

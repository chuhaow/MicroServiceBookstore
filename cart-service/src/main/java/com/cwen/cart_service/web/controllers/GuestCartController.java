package com.cwen.cart_service.web.controllers;


import com.cwen.cart_service.domain.GuestCartService;
import com.cwen.cart_service.domain.models.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts/guest")
public class GuestCartController {
    private final Logger log = LoggerFactory.getLogger(GuestCartController.class);

    private final GuestCartService guestCartService;

    public GuestCartController(GuestCartService guestCartService) {
        this.guestCartService = guestCartService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    AddToCartResponse addToCart(@Valid @RequestBody GuestAddToCartRequest request){
        log.info("Add item to cart for: Guest");
        return guestCartService.addToCart(request);
    }

    @PostMapping("/update/quantity")
    @ResponseStatus(HttpStatus.OK)
    UpdateItemQuantityResponse updateItemQuantity(@Valid @RequestBody GuestUpdateQuantityRequest request){
        log.info("Remove item from cart for: {}", request.guestId());
        return guestCartService.updateItemQuantity(request);
    }

    @GetMapping("/{guestId}")
    List<CartItemDTO> getCartItems(@PathVariable String guestId){
        log.info("Get cart items for: {}", guestId);
        return guestCartService.getCartItems(guestId);
    }

}

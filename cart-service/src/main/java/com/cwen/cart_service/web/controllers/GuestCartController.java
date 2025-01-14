package com.cwen.cart_service.web.controllers;


import com.cwen.cart_service.domain.CartService;
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

    private final CartService cartService;

    public GuestCartController(GuestCartService guestCartService, CartService cartService) {
        this.guestCartService = guestCartService;
        this.cartService = cartService;
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
        log.info("Update item from for: {}", request.guestId());
        return guestCartService.updateItemQuantity(request);
    }

    @GetMapping("/{guestId}")
    List<CartItemDTO> getCartItems(@PathVariable String guestId){
        log.info("Get cart items for: {}", guestId);
        return guestCartService.getCartItems(guestId);
    }

    @PostMapping("/{guestId}/{authUser}")
    @ResponseStatus(HttpStatus.OK)
    void testMerge(@PathVariable String guestId, @PathVariable String authUser){
        log.info("Merging");
        cartService.mergeGuestCart(guestId,authUser);
    }

}

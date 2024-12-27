package com.cwen.cart_service.web.controllers;


import com.cwen.cart_service.domain.GuestCartService;
import com.cwen.cart_service.domain.models.AddToCartRequest;
import com.cwen.cart_service.domain.models.AddToCartResponse;
import com.cwen.cart_service.domain.models.AddToGuestCartRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts/guest")
public class GuestCartController {
    private final Logger log = LoggerFactory.getLogger(GuestCartController.class);

    private final GuestCartService guestCartService;

    public GuestCartController(GuestCartService guestCartService) {
        this.guestCartService = guestCartService;
    }

    @PostMapping
    @ResponseStatus
    AddToCartResponse addToCart(@Valid @RequestBody AddToGuestCartRequest request){
        log.info("Add item to cart for: Guest");
        return guestCartService.addToCart(request);
    }
}

package com.cwen.cart_service.web;

import com.cwen.cart_service.domain.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final SecurityService securityService;

    public CartController(CartService cartService, SecurityService securityService) {
        this.cartService = cartService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateCartResponse createCart(@Valid @RequestBody CreateCartRequest request) {
        return cartService.createCart(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateCartResponse updateCart(@Valid @RequestBody UpdateCartRequest request){
        return cartService.updateCart(request);
    }
}

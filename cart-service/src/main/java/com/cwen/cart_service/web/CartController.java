package com.cwen.cart_service.web;

import com.cwen.cart_service.domain.*;
import com.cwen.cart_service.domain.models.AddToCartRequest;
import com.cwen.cart_service.domain.models.AddToCartResponse;
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
    @ResponseStatus(HttpStatus.OK)
    AddToCartResponse addToCart(@Valid @RequestBody AddToCartRequest request){
        String user = securityService.getLoginUsername();
        log.info("Add item to cart for: {}", user);

        return cartService.addToCart(request, user);
    }
}

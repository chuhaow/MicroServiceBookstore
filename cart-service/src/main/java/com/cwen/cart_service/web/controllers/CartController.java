package com.cwen.cart_service.web.controllers;

import com.cwen.cart_service.domain.*;
import com.cwen.cart_service.domain.models.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/carts")
@SecurityRequirement(name = "security_auth")
public class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final AuthCartService authCartService;
    private final CartService cartService;
    private final SecurityService securityService;

    public CartController(AuthCartService authCartService, CartService cartService, SecurityService securityService) {
        this.authCartService = authCartService;
        this.cartService = cartService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    AddToCartResponse addToCart(@Valid @RequestBody AddToCartRequest request){
        String user = securityService.getLoginUsername();
        log.info("Add item to cart for: {}", user);
        return authCartService.addToCart(request, user);
    }

    @PostMapping("/update/quantity")
    @ResponseStatus(HttpStatus.OK)
    UpdateItemQuantityResponse updateItemQuantity(@Valid @RequestBody UpdateItemQuantityRequest request){
        String user = securityService.getLoginUsername();
        log.info("Remove item from cart for: {}", user);
        return authCartService.updateItemQuantity(request, user);
    }

    @PostMapping("/merge/{guestId}")
    @ResponseStatus(HttpStatus.OK)
    List<CartItemDTO> mergeCart(@PathVariable String guestId){
        String user = securityService.getLoginUsername();
        cartService.mergeGuestCart(guestId,user);
        return authCartService.getCartItems(user);
    }

    @GetMapping
    List<CartItemDTO> getCartItems(){
        String user = securityService.getLoginUsername();
        log.info("Get cart items for: {}", user);
        return authCartService.getCartItems(user);
    }

    @DeleteMapping
    ResponseEntity<String> deleteUserCart(){
        String user = securityService.getLoginUsername();
        log.info("Delete user cart for: {}", user);
        authCartService.deleteCart(user);
        return ResponseEntity.ok("Cart deleted successfully.");
    }

}

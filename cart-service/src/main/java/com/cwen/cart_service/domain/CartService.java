package com.cwen.cart_service.domain;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    public CreateCartResponse createCart( CreateCartRequest request) {
        return null;
    }

    public UpdateCartResponse updateCart(@Valid UpdateCartRequest request) {
        return null;
    }
}

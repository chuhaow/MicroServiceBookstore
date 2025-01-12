package com.cwen.cart_service.domain;


import com.cwen.cart_service.catalog.Models.Product;
import com.cwen.cart_service.catalog.ProductServiceClient;
import com.cwen.cart_service.domain.exceptions.InvalidItemException;
import com.cwen.cart_service.domain.models.AddToCartRequest;
import com.cwen.cart_service.domain.models.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CartItemValidator {
    private static final Logger log = LoggerFactory.getLogger(CartItemValidator.class);

    private final ProductServiceClient productServiceClient;

    public CartItemValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public void validate(CartItem item){
        Product product = productServiceClient.getProductByCode(item.code())
                .orElseThrow(() -> new InvalidItemException("Invalid Product Code: " + item.code()));
        if(product.price().compareTo(item.price()) != 0){
            log.error("Product price is incorrect. Expected {}, got {}",product.price(),item.price());
            throw new InvalidItemException("Product price does not match");
        }
    }

}

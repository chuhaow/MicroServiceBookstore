package com.cwen.cart_service.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItemEntity extends AbstractItemEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;


    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

}

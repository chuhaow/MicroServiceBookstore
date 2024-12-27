package com.cwen.cart_service.domain.entities.auth;

import com.cwen.cart_service.domain.entities.AbstractItemEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class AuthCartItemEntity extends AbstractItemEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private AuthCartEntity cart;


    public AuthCartEntity getCart() {
        return cart;
    }

    public void setCart(AuthCartEntity cart) {
        this.cart = cart;
    }

}

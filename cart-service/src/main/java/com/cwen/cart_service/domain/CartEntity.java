package com.cwen.cart_service.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "carts")
public class CartEntity extends AbstractCartEntity {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart",orphanRemoval = true)
    private Set<CartItemEntity> items;

    public Set<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<CartItemEntity> items) {
        this.items = items;
    }

}

package com.cwen.cart_service.domain.entities.auth;

import com.cwen.cart_service.domain.entities.AbstractCartEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "carts")
public class AuthCartEntity extends AbstractCartEntity {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart",orphanRemoval = true)
    private Set<AuthCartItemEntity> items;

    public Set<AuthCartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<AuthCartItemEntity> items) {
        this.items = items;
    }

}

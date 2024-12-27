package com.cwen.cart_service.domain.entities.guest;

import com.cwen.cart_service.domain.entities.AbstractCartEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "guest_carts")
public class GuestCartEntity extends AbstractCartEntity {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guestCart",orphanRemoval = true)
    private Set<GuestCartItemEntity> items;

    public Set<GuestCartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<GuestCartItemEntity> items) {
        this.items = items;
    }
}


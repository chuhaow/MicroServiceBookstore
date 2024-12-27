package com.cwen.cart_service.domain.entities.guest;

import com.cwen.cart_service.domain.entities.AbstractItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "guest_cart_items")
public class GuestCartItemEntity extends AbstractItemEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private GuestCartEntity guestCart;

    public GuestCartEntity getCart() {
        return guestCart;
    }

    public void setCart(GuestCartEntity guestCart) {
        this.guestCart = guestCart;
    }

}

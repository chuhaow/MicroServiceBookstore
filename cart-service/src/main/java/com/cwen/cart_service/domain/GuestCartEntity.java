package com.cwen.cart_service.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
/*
@Entity
@Table(name = "guest_carts")
public class GuestCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guest_cart_id_generator")
    @SequenceGenerator(name = "guest_cart_id_generator", sequenceName = "guest_cart_id_seq")
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "guest_id", nullable=false)
    private String guestId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart",orphanRemoval = true)
    private Set<CartItemEntity> items;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String userId) {
        this.guestId = userId;
    }

    public Set<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<CartItemEntity> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

 */

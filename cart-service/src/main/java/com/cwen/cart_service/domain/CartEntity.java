package com.cwen.cart_service.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_id_generator")
    @SequenceGenerator(name = "cart_id_generator", sequenceName = "cart_id_seq")
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_id", nullable=false)
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

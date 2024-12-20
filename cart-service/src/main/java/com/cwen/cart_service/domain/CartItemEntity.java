package com.cwen.cart_service.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_id_generator")
    @SequenceGenerator(name = "cart_item_id_generator", sequenceName = "cart_item_id_seq")
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @Column(name ="product_code", nullable = false)
    private String code;

    @Column(name ="product_name", nullable = false)
    private String name;

    @Column(name ="product_price", nullable = false)
    private BigDecimal price;

    @Column(name ="quantity", nullable = false)
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

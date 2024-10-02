package com.cwen.catalog_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Product code cannot be empty")
    private String code;

    @NotEmpty(message = "Product must have a name")
    @Column(nullable = false)
    private String name;

    private String description;

    private String image_url;

    @NotNull(message = "Product must have a price")
    @DecimalMin("0.01")
    @Column(nullable = false)
    private BigDecimal price;

    public ProductEntity() {}

    public ProductEntity(Long id,String code, String name, String description, String imageURL, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.image_url = imageURL;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_url(String imageURL) {
        this.image_url = imageURL;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

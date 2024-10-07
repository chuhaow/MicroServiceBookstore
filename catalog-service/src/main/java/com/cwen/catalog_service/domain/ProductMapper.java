package com.cwen.catalog_service.domain;

class ProductMapper {
    static Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getCode(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImage_url(),
                productEntity.getPrice()
        );
    }
}

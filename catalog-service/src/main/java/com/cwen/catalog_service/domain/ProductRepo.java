package com.cwen.catalog_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByCode(String code);
}

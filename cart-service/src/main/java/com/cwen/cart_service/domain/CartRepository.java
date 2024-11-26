package com.cwen.cart_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}

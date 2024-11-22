package com.cwen.cart_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}

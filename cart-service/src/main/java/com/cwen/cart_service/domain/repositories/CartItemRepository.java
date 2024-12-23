package com.cwen.cart_service.domain.repositories;

import com.cwen.cart_service.domain.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
}

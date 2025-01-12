package com.cwen.cart_service.domain.repositories;

import com.cwen.cart_service.domain.entities.auth.AuthCartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<AuthCartItemEntity, Long> {
}

package com.cwen.cart_service.domain.repositories;

import com.cwen.cart_service.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestCartRepository extends JpaRepository<CartEntity, Long> {
}

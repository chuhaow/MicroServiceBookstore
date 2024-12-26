package com.cwen.cart_service.domain.repositories.guest;

import com.cwen.cart_service.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestCartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(String guestId);
}

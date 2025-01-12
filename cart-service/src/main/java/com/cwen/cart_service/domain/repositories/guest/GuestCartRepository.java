package com.cwen.cart_service.domain.repositories.guest;

import com.cwen.cart_service.domain.entities.guest.GuestCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface GuestCartRepository extends JpaRepository<GuestCartEntity, Long> {

    Optional<GuestCartEntity> findByUserId(String guestId);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}

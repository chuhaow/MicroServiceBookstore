package com.cwen.cart_service.domain.repositories.auth;

import com.cwen.cart_service.domain.entities.auth.AuthCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserCartRepository extends JpaRepository<AuthCartEntity, Long> {

    Optional<AuthCartEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}

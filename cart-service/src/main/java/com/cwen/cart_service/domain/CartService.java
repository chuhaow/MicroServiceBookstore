package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.entities.auth.AuthCartEntity;
import com.cwen.cart_service.domain.entities.auth.AuthCartItemEntity;
import com.cwen.cart_service.domain.entities.guest.GuestCartEntity;
import com.cwen.cart_service.domain.entities.guest.GuestCartItemEntity;
import com.cwen.cart_service.domain.repositories.auth.AuthUserCartRepository;
import com.cwen.cart_service.domain.repositories.guest.GuestCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final GuestCartRepository guestCartRepository;
    private final AuthUserCartRepository authUserCartRepository;

    public CartService(GuestCartRepository guestCartRepository, AuthUserCartRepository authUserCartRepository) {
        this.guestCartRepository = guestCartRepository;
        this.authUserCartRepository = authUserCartRepository;
    }

    public void mergeGuestCart(String guestId, String authUserId){
        log.info("Merging items for guestID: {} with contains of authUserId: {} cart", guestId, authUserId);
        GuestCartEntity guestCart = guestCartRepository.findByUserId(guestId)
                .orElseThrow(() -> new RuntimeException("Cart not found for guest ID: " + guestId));

        AuthCartEntity authCart = authUserCartRepository.findByUserId(authUserId)
                .orElseGet( () -> {
                    AuthCartEntity newCart = new AuthCartEntity();
                    newCart.setUserId(authUserId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new HashSet<>());
                    return newCart;
                });

        List<GuestCartItemEntity> guestItems = guestCart.getItems().stream().toList();
        Set<AuthCartItemEntity> authCartItems = authCart.getItems();

        // Merge items
        for (GuestCartItemEntity guestItem : guestItems) {
            Optional<AuthCartItemEntity> matchingItem = authCartItems.stream()
                    .filter(authItem -> authItem.getCode().equals(guestItem.getCode()))
                    .findFirst();

            if (matchingItem.isPresent()) {

                AuthCartItemEntity authItem = matchingItem.get();
                authItem.setQuantity(authItem.getQuantity() + guestItem.getQuantity());
            } else {

                AuthCartItemEntity newAuthItem = new AuthCartItemEntity();
                newAuthItem.setCode(guestItem.getCode());
                newAuthItem.setName(guestItem.getName());
                newAuthItem.setQuantity(guestItem.getQuantity());
                newAuthItem.setPrice(guestItem.getPrice());
                newAuthItem.setCart(authCart);
                authCartItems.add(newAuthItem);

            }
        }

        authUserCartRepository.save(authCart);
        guestCartRepository.delete(guestCart);


    }

}

package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.models.AddToCartRequest;
import com.cwen.cart_service.domain.models.AddToCartResponse;
import com.cwen.cart_service.domain.models.AddToGuestCartRequest;
import com.cwen.cart_service.domain.models.CartItem;
import com.cwen.cart_service.domain.repositories.guest.GuestCartRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
/*
@Service
public class GuestCartService {
    private final Logger log = LoggerFactory.getLogger(GuestCartService.class);

    private final GuestCartRepository guestCartRepository;
    private final CartItemValidator cartItemValidator;

    public GuestCartService(GuestCartRepository guestCartRepository,CartItemValidator cartItemValidator) {
        this.guestCartRepository = guestCartRepository;
        this.cartItemValidator = cartItemValidator;
    }

    public AddToCartResponse addToCart(@Valid AddToGuestCartRequest request) {
        CartItem cartItem = request.item();
        String guestId = request.guestId();
        cartItemValidator.validate(cartItem);


        CartEntity cart = guestCartRepository.findByGuestId(guestId)
                .orElseGet( () -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUserId(guestId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new HashSet<>());
                    return newCart;
                });

        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getCode().equals(cartItem.code()))
                .findFirst();

        if(existingItem.isPresent()) {
            CartItemEntity itemEntity = existingItem.get();
            itemEntity.setQuantity(itemEntity.getQuantity() + cartItem.quantity());
        }else{
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setCode(cartItem.code());
            cartItemEntity.setName(cartItem.name());
            cartItemEntity.setPrice(cartItem.price());
            cartItemEntity.setQuantity(cartItem.quantity());
            cartItemEntity.setCart(cart);
            cart.getItems().add(cartItemEntity);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        CartEntity savedCart = guestCartRepository.save(cart);
        return new AddToCartResponse(savedCart.getId());

    }
}

 */

package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.entities.auth.AuthCartEntity;
import com.cwen.cart_service.domain.entities.auth.AuthCartItemEntity;
import com.cwen.cart_service.domain.entities.guest.GuestCartEntity;
import com.cwen.cart_service.domain.entities.guest.GuestCartItemEntity;
import com.cwen.cart_service.domain.exceptions.ItemNotFoundInCartException;
import com.cwen.cart_service.domain.exceptions.UserIdNotFound;
import com.cwen.cart_service.domain.models.*;
import com.cwen.cart_service.domain.repositories.guest.GuestCartRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GuestCartService {
    private final Logger log = LoggerFactory.getLogger(GuestCartService.class);

    private final GuestCartRepository guestCartRepository;
    private final CartItemValidator cartItemValidator;

    public GuestCartService(GuestCartRepository guestCartRepository,CartItemValidator cartItemValidator) {
        this.guestCartRepository = guestCartRepository;
        this.cartItemValidator = cartItemValidator;
    }

    public AddToCartResponse addToCart(@Valid GuestAddToCartRequest request) {
        CartItem cartItem = request.item();
        String guestId = request.guestId();
        cartItemValidator.validate(cartItem);


        GuestCartEntity cart = guestCartRepository.findByUserId(guestId)
                .orElseGet( () -> {
                    GuestCartEntity newCart = new GuestCartEntity();
                    newCart.setUserId(guestId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new HashSet<>());
                    return newCart;
                });

        Optional<GuestCartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getCode().equals(cartItem.code()))
                .findFirst();

        if(existingItem.isPresent()) {
            GuestCartItemEntity itemEntity = existingItem.get();
            itemEntity.setQuantity(itemEntity.getQuantity() + cartItem.quantity());
        }else{
            GuestCartItemEntity cartItemEntity = new GuestCartItemEntity();
            cartItemEntity.setCode(cartItem.code());
            cartItemEntity.setName(cartItem.name());
            cartItemEntity.setPrice(cartItem.price());
            cartItemEntity.setQuantity(cartItem.quantity());
            cartItemEntity.setCart(cart);
            cart.getItems().add(cartItemEntity);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        GuestCartEntity savedCart = guestCartRepository.save(cart);
        return new AddToCartResponse(savedCart.getId());

    }

    public UpdateItemQuantityResponse updateItemQuantity(@Valid GuestUpdateQuantityRequest request) {
        String guestId = request.guestId();
        String code = request.itemCode();
        Integer quantity = request.quantity();

        GuestCartEntity cart = guestCartRepository.findByUserId(guestId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + guestId));


        Optional<GuestCartItemEntity> itemToUpdateOpt = cart.getItems().stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst();

        if (itemToUpdateOpt.isPresent()) {
            GuestCartItemEntity itemToUpdate = itemToUpdateOpt.get();

            if (quantity == 0) {
                cart.getItems().remove(itemToUpdate);
            }else{
                itemToUpdate.setQuantity(quantity);
            }

            cart.setUpdatedAt(LocalDateTime.now());
            GuestCartEntity savedCart = guestCartRepository.save(cart);

            return new UpdateItemQuantityResponse(code, savedCart.getId());
        } else {
            throw new ItemNotFoundInCartException("Item not found in the cart for user ID: " + guestId);
        }
    }

    public List<CartItemDTO> getCartItems(String guestId) {
        List<GuestCartEntity> carts = guestCartRepository.findAll();
        Optional<GuestCartEntity> cart = guestCartRepository.findByUserId(guestId);
        if(cart.isEmpty()){
            log.warn("Not found cart for guest ID: " + guestId);
        }

        return cart.map(c -> c.getItems().stream()
                        .map(item -> new CartItemDTO(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }
}


package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.entities.auth.AuthCartEntity;
import com.cwen.cart_service.domain.entities.auth.AuthCartItemEntity;
import com.cwen.cart_service.domain.exceptions.ItemNotFoundInCartException;
import com.cwen.cart_service.domain.models.*;
import com.cwen.cart_service.domain.repositories.auth.AuthUserCartRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthCartService {
    private static Logger logger = LoggerFactory.getLogger(AuthCartService.class);

    private final AuthUserCartRepository authUserCartRepository;
    private final CartItemValidator cartItemValidator;

    public AuthCartService(final AuthUserCartRepository authUserCartRepository, final CartItemValidator cartItemValidator) {
        this.authUserCartRepository = authUserCartRepository;
        this.cartItemValidator = cartItemValidator;
    }

    public AddToCartResponse addToCart(@Valid AddToCartRequest request, String user) {
        CartItem cartItem = request.item();
        cartItemValidator.validate(cartItem);


        AuthCartEntity cart = authUserCartRepository.findByUserId(user)
                .orElseGet( () -> {
                    AuthCartEntity newCart = new AuthCartEntity();
                    newCart.setUserId(user);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new HashSet<>());
                    return newCart;
                });

        Optional<AuthCartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getCode().equals(cartItem.code()))
                .findFirst();

        if(existingItem.isPresent()) {
            AuthCartItemEntity itemEntity = existingItem.get();
            itemEntity.setQuantity(itemEntity.getQuantity() + cartItem.quantity());
        }else{
            AuthCartItemEntity authCartItemEntity = new AuthCartItemEntity();
            authCartItemEntity.setCode(cartItem.code());
            authCartItemEntity.setName(cartItem.name());
            authCartItemEntity.setPrice(cartItem.price());
            authCartItemEntity.setQuantity(cartItem.quantity());
            authCartItemEntity.setCart(cart);
            cart.getItems().add(authCartItemEntity);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        AuthCartEntity savedCart = authUserCartRepository.save(cart);
        return new AddToCartResponse(savedCart.getId());
    }

    public UpdateItemQuantityResponse updateItemQuantity(@Valid UpdateItemQuantityRequest request, String user){
        String code = request.itemCode();
        Integer quantity = request.quantity();

        AuthCartEntity cart = authUserCartRepository.findByUserId(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + user));


        Optional<AuthCartItemEntity> itemToUpdateOpt = cart.getItems().stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst();

        if (itemToUpdateOpt.isPresent()) {
            AuthCartItemEntity itemToUpdate = itemToUpdateOpt.get();

            if (quantity == 0) {
                cart.getItems().remove(itemToUpdate);
            }else{
                itemToUpdate.setQuantity(quantity);
            }

            cart.setUpdatedAt(LocalDateTime.now());
            AuthCartEntity savedCart = authUserCartRepository.save(cart);

            return new UpdateItemQuantityResponse(code, savedCart.getId());
        } else {
            throw new ItemNotFoundInCartException("Item not found in the cart for user ID: " + user);
        }
    }

    public List<CartItemDTO> getCartItems(String userId) {
        Optional<AuthCartEntity> cart = authUserCartRepository.findByUserId(userId);
        return cart.map(c -> c.getItems().stream()
                        .map(item -> new CartItemDTO(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }


    public void deleteCart(String userId) {
        authUserCartRepository.deleteByUserId(userId);
    }
}

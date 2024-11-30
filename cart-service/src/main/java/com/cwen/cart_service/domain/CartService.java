package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.models.*;
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
public class CartService {
    private static Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    public CartService(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public AddToCartResponse addToCart(@Valid AddToCartRequest request, String user) {
        String userId = request.userId();
        CartItem cartItem = request.item();

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet( () -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUserId(userId);
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
        CartEntity savedCart = cartRepository.save(cart);
        return new AddToCartResponse(savedCart.getId());
    }

    public UpdateItemQuantityResponse updateItemQuantity(@Valid UpdateItemQuantityRequest request, String user){
        String userId = request.userId();
        CartItem cartItem = request.item();

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));


        Optional<CartItemEntity> itemToUpdateOpt = cart.getItems().stream()
                .filter(item -> item.getCode().equals(cartItem.code()))
                .findFirst();

        if (itemToUpdateOpt.isPresent()) {
            CartItemEntity itemToUpdate = itemToUpdateOpt.get();

            if (cartItem.quantity() == 0) {
                cart.getItems().remove(itemToUpdate);
            }else{
                itemToUpdate.setQuantity(cartItem.quantity());
            }

            cart.setUpdatedAt(LocalDateTime.now());
            CartEntity savedCart = cartRepository.save(cart);

            return new UpdateItemQuantityResponse(cartItem.code(), savedCart.getId());
        } else {
            throw new RuntimeException("Item not found in the cart for user ID: " + userId);
        }
    }

    public List<CartItemDTO> getCartItems(String userId) {
        Optional<CartEntity> cart = cartRepository.findByUserId(userId);
        return cart.map(c -> c.getItems().stream()
                        .map(item -> new CartItemDTO(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }


    public void deleteCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}

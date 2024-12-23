package com.cwen.cart_service.domain;

import com.cwen.cart_service.domain.exceptions.ItemNotFoundInCartException;
import com.cwen.cart_service.domain.models.*;
import com.cwen.cart_service.domain.repositories.AuthUserCartRepository;
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

    private final AuthUserCartRepository authUserCartRepository;
    private final CartItemValidator cartItemValidator;

    public CartService(final AuthUserCartRepository authUserCartRepository, final CartItemValidator cartItemValidator) {
        this.authUserCartRepository = authUserCartRepository;
        this.cartItemValidator = cartItemValidator;
    }

    public AddToCartResponse addToCart(@Valid AddToCartRequest request, String user) {
        cartItemValidator.validate(request);
        CartItem cartItem = request.item();

        CartEntity cart = authUserCartRepository.findByUserId(user)
                .orElseGet( () -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUserId(user);
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
        CartEntity savedCart = authUserCartRepository.save(cart);
        return new AddToCartResponse(savedCart.getId());
    }

    public UpdateItemQuantityResponse updateItemQuantity(@Valid UpdateItemQuantityRequest request, String user){
        String code = request.itemCode();
        Integer quantity = request.quantity();

        CartEntity cart = authUserCartRepository.findByUserId(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + user));


        Optional<CartItemEntity> itemToUpdateOpt = cart.getItems().stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst();

        if (itemToUpdateOpt.isPresent()) {
            CartItemEntity itemToUpdate = itemToUpdateOpt.get();

            if (quantity == 0) {
                cart.getItems().remove(itemToUpdate);
            }else{
                itemToUpdate.setQuantity(quantity);
            }

            cart.setUpdatedAt(LocalDateTime.now());
            CartEntity savedCart = authUserCartRepository.save(cart);

            return new UpdateItemQuantityResponse(code, savedCart.getId());
        } else {
            throw new ItemNotFoundInCartException("Item not found in the cart for user ID: " + user);
        }
    }

    public List<CartItemDTO> getCartItems(String userId) {
        Optional<CartEntity> cart = authUserCartRepository.findByUserId(userId);
        return cart.map(c -> c.getItems().stream()
                        .map(item -> new CartItemDTO(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }


    public void deleteCart(String userId) {
        authUserCartRepository.deleteByUserId(userId);
    }
}

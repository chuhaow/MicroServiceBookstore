package com.cwen.cart_service.services;

import com.cwen.cart_service.AbstractIntegrationTest;
import com.cwen.cart_service.domain.CartService;
import com.cwen.cart_service.domain.entities.auth.AuthCartEntity;
import com.cwen.cart_service.domain.entities.auth.AuthCartItemEntity;
import com.cwen.cart_service.domain.repositories.auth.AuthUserCartRepository;
import com.cwen.cart_service.domain.repositories.guest.GuestCartRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Sql(scripts = "/test-guest-cart.sql",
        config = @SqlConfig(dataSource = "testGuestDataSource", transactionManager = "testGuestTransactionManager"))
@Sql("/test-cart.sql")
@Transactional
public class CartServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GuestCartRepository guestCartRepository;

    @Autowired
    private AuthUserCartRepository authUserCartRepository;

    @Autowired
    private CartService cartService;

    @Test
    void OverlappingCartTest(){
        String guestId = "abcd12344321";
        String authUserId = "test";

        cartService.mergeGuestCart(guestId, authUserId);

        AuthCartEntity updatedAuthCart = authUserCartRepository.findByUserId(authUserId)
                .orElseThrow(() -> new AssertionError("Auth cart not found"));
        AuthCartItemEntity item = updatedAuthCart.getItems().stream()
                .filter(i -> "P100".equals(i.getCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item with code P100 not found"));

        assertEquals(1, updatedAuthCart.getItems().size());
        assertTrue(updatedAuthCart.getItems().stream().anyMatch(i -> "P100".equals(i.getCode())));


        assertEquals(Integer.valueOf(2), item.getQuantity());
        assertTrue(guestCartRepository.findByUserId(guestId).isEmpty());
    }

    @Test
    void NonOverlappingCartTest(){
        String guestId = "1234abcd4321";
        String authUserId = "test";

        cartService.mergeGuestCart(guestId, authUserId);

        AuthCartEntity updatedAuthCart = authUserCartRepository.findByUserId(authUserId)
                .orElseThrow(() -> new AssertionError("Auth cart not found"));



        assertEquals(2, updatedAuthCart.getItems().size());
        assertTrue(updatedAuthCart.getItems().stream().anyMatch(i -> "P100".equals(i.getCode())));
        assertTrue(updatedAuthCart.getItems().stream().anyMatch(i -> "P500".equals(i.getCode())));


        assertTrue(guestCartRepository.findByUserId(guestId).isEmpty());
    }

    @Test
    void EmptyGuestCartTest(){
        String guestId = "EmptyGuestCart";
        String authUserId = "test";

        cartService.mergeGuestCart(guestId, authUserId);

        AuthCartEntity updatedAuthCart = authUserCartRepository.findByUserId(authUserId)
                .orElseThrow(() -> new AssertionError("Auth cart not found"));
        AuthCartItemEntity item = updatedAuthCart.getItems().stream()
                .filter(i -> "P100".equals(i.getCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item with code P100 not found"));



        assertEquals(1, updatedAuthCart.getItems().size());
        assertTrue(updatedAuthCart.getItems().stream().anyMatch(i -> "P100".equals(i.getCode())));

        assertEquals(Integer.valueOf(1), item.getQuantity());
        assertTrue(guestCartRepository.findByUserId(guestId).isEmpty());

    }

    @Test
    void NoAuthCartTest(){
        String guestId = "abcd12344321";
        String authUserId = "NewCart";

        cartService.mergeGuestCart(guestId, authUserId);

        AuthCartEntity updatedAuthCart = authUserCartRepository.findByUserId(authUserId)
                .orElseThrow(() -> new AssertionError("Auth cart not found"));
        AuthCartItemEntity item = updatedAuthCart.getItems().stream()
                .filter(i -> "P100".equals(i.getCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item with code P100 not found"));

        assertEquals(1, updatedAuthCart.getItems().size());
        assertTrue(updatedAuthCart.getItems().stream().anyMatch(i -> "P100".equals(i.getCode())));


        assertEquals(Integer.valueOf(1), item.getQuantity());
        assertTrue(guestCartRepository.findByUserId(guestId).isEmpty());
    }

    @Test
    void invalidGuestIdTest(){
        String guestId = "invalidGuestId";
        String authUserId = "test";

        assertThrows(RuntimeException.class, () -> {
            cartService.mergeGuestCart(guestId, authUserId);
        }, "Cart not found for guest ID: " + guestId);
    }
}

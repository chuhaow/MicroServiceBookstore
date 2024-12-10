package com.cwen.cart_service.domain.exceptions;

public class ItemNotFoundInCartException extends RuntimeException {
    public ItemNotFoundInCartException(String message) {
        super(message);
    }
}

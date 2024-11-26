package com.cwen.cart_service.domain.exceptions;

public class InvalidCartRequestException extends RuntimeException {
    public InvalidCartRequestException(String message) {
        super(message);
    }
}

package com.cwen.cart_service.domain.exceptions;

public class UserIdNotFound extends RuntimeException {
    public UserIdNotFound(String message) {
        super(message);
    }
}

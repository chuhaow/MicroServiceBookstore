package com.cwen.order_service.web.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){super(message);}

    public static OrderNotFoundException forOrderNumber(String orderNumber){
        return new OrderNotFoundException("Cannot found order number: " + orderNumber);
    }
}

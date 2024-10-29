package com.cwen.order_service.domain.models;

//Todo: contain more information such as items
public record OrderSummary(String orderNumber, OrderStatus status) {
}

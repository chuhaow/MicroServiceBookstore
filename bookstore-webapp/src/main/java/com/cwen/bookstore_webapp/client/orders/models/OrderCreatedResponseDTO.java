package com.cwen.bookstore_webapp.client.orders.models;

public record OrderCreatedResponseDTO(String orderNum, OrderStatus orderStatus) {
}

package com.cwen.bookstore_webapp.client.orders.models;

//Todo: contain more information such as items
public record OrderSummary(String orderNumber, OrderStatus status) {
}

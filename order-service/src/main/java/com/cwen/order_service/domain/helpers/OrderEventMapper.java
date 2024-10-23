package com.cwen.order_service.domain.helpers;

import com.cwen.order_service.domain.OrderEntity;
import com.cwen.order_service.domain.models.OrderItem;
import com.cwen.order_service.domain.models.events.OrderCancelledEvent;
import com.cwen.order_service.domain.models.events.OrderCreatedEvent;
import com.cwen.order_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.order_service.domain.models.events.OrderErrorEvent;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventMapper {

    public static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity order) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                LocalDateTime.now()
        );
    }

    public static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity order, LocalDateTime deliveryTime) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                deliveryTime,
                LocalDateTime.now()

        );
    }

    public static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity order, String reason){
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                reason,
                LocalDateTime.now()
        );
    }

    public static OrderErrorEvent buildOrderErrorEvent(OrderEntity order, String reason){
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                reason,
                LocalDateTime.now()
        );
    }

    private static Set<OrderItem> getOrderItems(OrderEntity order) {
        return order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(),
                        item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
    }
}
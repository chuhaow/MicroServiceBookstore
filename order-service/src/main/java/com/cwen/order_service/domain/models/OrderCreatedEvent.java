package com.cwen.order_service.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) { }

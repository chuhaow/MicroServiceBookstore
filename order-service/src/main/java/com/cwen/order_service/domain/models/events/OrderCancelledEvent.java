package com.cwen.order_service.domain.models.events;

import com.cwen.order_service.domain.models.Address;
import com.cwen.order_service.domain.models.Customer;
import com.cwen.order_service.domain.models.OrderItem;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address shippingAddress,
        String reason,
        LocalDateTime createdAt) { }

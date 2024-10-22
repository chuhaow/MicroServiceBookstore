package com.cwen.order_service.domain.models.events;

import java.time.LocalDateTime;
import java.util.Set;

import com.cwen.order_service.domain.models.Address;
import com.cwen.order_service.domain.models.Customer;
import com.cwen.order_service.domain.models.OrderItem;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) { }

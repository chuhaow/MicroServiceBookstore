package com.cwen.order_service.domain.models.events;

import com.cwen.order_service.domain.models.Address;
import com.cwen.order_service.domain.models.Customer;
import com.cwen.order_service.domain.models.OrderItem;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderDeliveredEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime deliveredTime,
        LocalDateTime createdAt) { }

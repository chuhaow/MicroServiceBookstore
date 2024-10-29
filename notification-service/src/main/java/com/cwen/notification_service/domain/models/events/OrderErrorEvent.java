package com.cwen.notification_service.domain.models.events;

import com.cwen.notification_service.domain.models.Address;
import com.cwen.notification_service.domain.models.Customer;
import com.cwen.notification_service.domain.models.OrderItem;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) { }

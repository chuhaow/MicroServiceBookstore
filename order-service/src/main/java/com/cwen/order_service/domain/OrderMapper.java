package com.cwen.order_service.domain;


import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.OrderItem;
import com.cwen.order_service.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class OrderMapper {
    static OrderEntity toEntity(CreateOrderRequest req) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomer(req.customer());
        entity.setDeliveryAddress(req.address());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for(OrderItem item : req.items()) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setCode(item.code());
            orderItemEntity.setName(item.name());
            orderItemEntity.setPrice(item.price());
            orderItemEntity.setQuantity(item.quantity());
            orderItemEntity.setOrder(entity);
            orderItems.add(orderItemEntity);
        }
        entity.setItems(orderItems);
        return entity;
    }
}

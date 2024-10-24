package com.cwen.order_service.domain;


import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.OrderDTO;
import com.cwen.order_service.domain.models.OrderItem;
import com.cwen.order_service.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    static OrderDTO toDataTransferObject(OrderEntity entity) {
        Set<OrderItem> orderItems = entity.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
        return new OrderDTO(
                entity.getOrderNumber(),
                entity.getUsername(),
                orderItems,
                entity.getCustomer(),
                entity.getDeliveryAddress(),
                entity.getStatus(),
                entity.getComments(),
                entity.getCreatedAt()
        );
    }
}

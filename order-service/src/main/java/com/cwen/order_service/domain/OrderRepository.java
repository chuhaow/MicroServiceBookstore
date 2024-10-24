package com.cwen.order_service.domain;

import com.cwen.order_service.domain.models.OrderStatus;
import com.cwen.order_service.web.exception.OrderNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default  void updateOrderStatus(String orderId, OrderStatus status) {
        Optional<OrderEntity> orderEntity = findByOrderNumber(orderId);
        if (orderEntity.isPresent()) {
            orderEntity.get().setStatus(status);
            this.save(orderEntity.get());
        }else{
            throw new OrderNotFoundException("Cannot update status for Order " + orderId + ". Order Not Found");
        }
    }
}

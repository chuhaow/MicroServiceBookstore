package com.cwen.order_service.domain;

import com.cwen.order_service.domain.models.OrderStatus;
import com.cwen.order_service.domain.models.OrderSummary;
import com.cwen.order_service.web.exception.OrderNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    @Query("""
        select new com.cwen.order_service.domain.models.OrderSummary(o.orderNumber, o.status)
        from OrderEntity o
        where o.username = :username
    """)
    List<OrderSummary> findByUsername(String username);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("""
        select distinct o 
        from OrderEntity o left join fetch o.items
        where o.username = :username and o.orderNumber = :orderNumber
    """)
    Optional<OrderEntity> findByUsernameAndOrderNumber(String username, String orderNumber);

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

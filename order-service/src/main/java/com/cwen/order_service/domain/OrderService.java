package com.cwen.order_service.domain;

import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse createOrder(String username, CreateOrderRequest req){
        OrderEntity newOrder = OrderMapper.toEntity(req);
        newOrder.setUsername(username);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Order created: {}", savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}

package com.cwen.order_service.domain;

import com.cwen.order_service.domain.helpers.OrderEventMapper;
import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.CreateOrderResponse;
import com.cwen.order_service.domain.models.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String username, CreateOrderRequest req){
        orderValidator.validate(req);
        OrderEntity newOrder = OrderMapper.toEntity(req);
        newOrder.setUsername(username);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Order created: {}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}

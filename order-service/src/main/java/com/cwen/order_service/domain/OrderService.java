package com.cwen.order_service.domain;

import com.cwen.order_service.domain.helpers.OrderEventMapper;
import com.cwen.order_service.domain.models.*;
import com.cwen.order_service.domain.models.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ZONES = List.of("CANADA", "USA", "JAPAN", "UK");

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public List<OrderSummary> getOrders(String username){
        return orderRepository.findByUsername(username);
    }

    public Optional<OrderDTO> getUserOrder(String username, String orderNumber){
        return orderRepository.findByUsernameAndOrderNumber(username,orderNumber)
                .map(OrderMapper::toDataTransferObject);
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

    public void processNewOrders(){
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);

        for(OrderEntity order : orders){
            this.process(order);
        }
    }

    private void process(OrderEntity order){
        try{
            if(withInDeliveryZone(order)){
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order, LocalDateTime.now()));
            }else{
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order, "Delivery location not within Delivery Zone"));

            }
        }catch (RuntimeException e){
            log.error("Failed to process Order with Order Number: {}", order.getOrderNumber(),e);
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order,e.getMessage()));
        }
    }

    private boolean withInDeliveryZone(OrderEntity order){
        return DELIVERY_ZONES.contains(order.getDeliveryAddress().country().toUpperCase());
    }
}

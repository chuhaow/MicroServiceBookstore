package com.cwen.order_service.domain;

import com.cwen.order_service.ApplicationProperties;
import com.cwen.order_service.domain.models.events.OrderCancelledEvent;
import com.cwen.order_service.domain.models.events.OrderCreatedEvent;
import com.cwen.order_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.order_service.domain.models.events.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    private void send(String routingKey, Object payload){
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), routingKey, payload);
    }

    public void publish(OrderCreatedEvent event){
        this.send(applicationProperties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event){
        this.send(applicationProperties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event){
        this.send(applicationProperties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event){
        this.send(applicationProperties.errorOrdersQueue(), event);
    }

}

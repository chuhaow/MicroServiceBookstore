package com.cwen.notification_service.handlers;

import com.cwen.notification_service.domain.NotificationService;
import com.cwen.notification_service.domain.models.OrderEventEntity;
import com.cwen.notification_service.domain.models.OrderEventRepository;
import com.cwen.notification_service.domain.models.events.OrderCancelledEvent;
import com.cwen.notification_service.domain.models.events.OrderCreatedEvent;
import com.cwen.notification_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.notification_service.domain.models.events.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    private final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;

    private final OrderEventRepository orderEventRepository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    private void handleNewOrderEvent(OrderCreatedEvent event) {
       log.info("Order New: " + event.eventId());
        if(orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order eventID: {}. This will be skipped", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    private void handleDeliveredOrderEvent(OrderDeliveredEvent event) {
        log.info("Order Delivered: " + event.eventId());
        if(orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order eventID: {}. This will be skipped", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    private void handleCancelledOrderEvent(OrderCancelledEvent event) {
        log.info("Order Cancelled: " + event.eventId());
        if(orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order eventID: {}. This will be skipped", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    private void handleErrorOrderEvent(OrderErrorEvent event) {
        log.info("Order Error: " + event.reason());
        if(orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order eventID: {}. This will be skipped", event.eventId());
            return;
        }
        notificationService.sendOrderErrorNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }
}

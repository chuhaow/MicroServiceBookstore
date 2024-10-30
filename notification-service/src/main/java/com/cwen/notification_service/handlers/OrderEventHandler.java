package com.cwen.notification_service.handlers;

import com.cwen.notification_service.domain.NotificationService;
import com.cwen.notification_service.domain.models.events.OrderCancelledEvent;
import com.cwen.notification_service.domain.models.events.OrderCreatedEvent;
import com.cwen.notification_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.notification_service.domain.models.events.OrderErrorEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final NotificationService notificationService;

    public OrderEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    private void handleNewOrderEvent(OrderCreatedEvent event) {
        System.out.println("Order New: " + event.eventId());
        notificationService.sendOrderCreatedNotification(event);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    private void handleDeliveredOrderEvent(OrderDeliveredEvent event) {
        System.out.println("Order Delivered: " + event.eventId());
        notificationService.sendOrderDeliveredNotification(event);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    private void handleCancelledOrderEvent(OrderCancelledEvent event) {
        System.out.println("Order Cancelled: " + event.eventId());
        notificationService.sendOrderCancelledNotification(event);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    private void handleErrorOrderEvent(OrderErrorEvent event) {
        System.out.println("Order Error: " + event.reason());
        notificationService.sendOrderErrorNotification(event);
    }
}

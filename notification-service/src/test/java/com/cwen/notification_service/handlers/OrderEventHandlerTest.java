package com.cwen.notification_service.handlers;

import com.cwen.notification_service.AbstractIntegrationTest;
import com.cwen.notification_service.ApplicationProperties;
import com.cwen.notification_service.domain.models.Address;
import com.cwen.notification_service.domain.models.Customer;
import com.cwen.notification_service.domain.models.events.OrderCancelledEvent;
import com.cwen.notification_service.domain.models.events.OrderCreatedEvent;
import com.cwen.notification_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.notification_service.domain.models.events.OrderErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.reset;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

public class OrderEventHandlerTest extends AbstractIntegrationTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ApplicationProperties applicationProperties;

    Customer customer = new Customer("John", "email@email.com", "12312341234");
    Address address = new Address("Street st", "","city", "state","2160033", "Country");

    @Test
    void handleOrderCreatedEvent(){
        String orderNumber = UUID.randomUUID().toString();

        var event = new OrderCreatedEvent(UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, LocalDateTime.now());
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted( () ->{
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }

    @Test
    void handleOrderDeliveredEvent(){
        String orderNumber = UUID.randomUUID().toString();

        var event = new OrderDeliveredEvent(UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, LocalDateTime.now(),  LocalDateTime.now());
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted( () ->{
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }

    @Test
    void handleOrderCancelledEvent(){
        String orderNumber = UUID.randomUUID().toString();

        var event = new OrderCancelledEvent(UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, "Reason",  LocalDateTime.now());
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted( () ->{
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }

    @Test
    void handleOrderErrorEvent(){
        String orderNumber = UUID.randomUUID().toString();

        var event = new OrderErrorEvent(UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, "Reason",  LocalDateTime.now());
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted( () ->{
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }





    @Test
    void handleDuplicateOrderCreatedEvent() {
        String orderNumber = UUID.randomUUID().toString();
        String eventId = UUID.randomUUID().toString();

        OrderCreatedEvent event = new OrderCreatedEvent(eventId, orderNumber, Set.of(), customer, address, LocalDateTime.now());

        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(notificationService).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });

        reset(notificationService);

        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), applicationProperties.newOrdersQueue(), event);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(notificationService, times(0)).sendOrderCreatedNotification(any(OrderCreatedEvent.class));
        });
    }
}

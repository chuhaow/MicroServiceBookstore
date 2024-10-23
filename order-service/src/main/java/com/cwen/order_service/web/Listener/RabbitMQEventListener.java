package com.cwen.order_service.web.Listener;

import com.cwen.order_service.domain.models.events.OrderErrorEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQEventListener {
    @RabbitListener(queues = "${orders.error-orders-queue}")
    public void handleErrorEvent(OrderErrorEvent event) {
        //TODO: Actual send this to some kind of support service later
        System.out.println("Order Error: " + event.reason());
    }
}

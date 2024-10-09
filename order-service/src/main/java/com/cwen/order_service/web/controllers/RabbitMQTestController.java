package com.cwen.order_service.web.controllers;

import com.cwen.order_service.ApplicationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQTestController {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    RabbitMQTestController(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message){
        System.out.println("send message");
        MyPayload payload = new MyPayload(message.payload().content());
        rabbitTemplate.convertAndSend(
                properties.orderEventsExchange(),
                message.routingKey(),
                payload
        );
    }
}

record MyMessage(String routingKey, MyPayload payload){}

record MyPayload(String content){}
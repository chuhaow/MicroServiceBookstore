package com.cwen.order_service.config;

import com.cwen.order_service.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private final ApplicationProperties applicationProperties;

    RabbitMQConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    DirectExchange orderServiceExchange() {
        DirectExchange exchange = new DirectExchange(applicationProperties.orderEventsExchange());
        exchange.setShouldDeclare(true);
        System.out.println("Creating exchange");
        return exchange;
    }

    @Bean
    Queue newOrdersQueue(){
        return QueueBuilder.durable(applicationProperties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrdersBinding() {
        return BindingBuilder.bind(newOrdersQueue()).to(orderServiceExchange()).with(applicationProperties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrdersQueue(){
        return QueueBuilder.durable(applicationProperties.deliveredOrdersQueue()).build();
    }

    @Bean
    Binding deliveredOrdersBinding() {
        return BindingBuilder.bind(deliveredOrdersQueue()).to(orderServiceExchange()).with(applicationProperties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrdersQueue(){
        return QueueBuilder.durable(applicationProperties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding cancelledOrdersQueueBinding(){
        return BindingBuilder.bind(cancelledOrdersQueue()).to(orderServiceExchange()).with(applicationProperties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrdersQueue(){
        return QueueBuilder.durable(applicationProperties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrdersBinding() {
        return BindingBuilder.bind(errorOrdersQueue()).to(orderServiceExchange()).with(applicationProperties.errorOrdersQueue());
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }
}

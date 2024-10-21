package com.cwen.order_service.domain;

import com.cwen.order_service.domain.models.events.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderEventService {
    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    public OrderEventService(OrderEventRepository orderEventRepository, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper){
        this.orderEventRepository = orderEventRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    void save(OrderCreatedEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    public void publishOrderEvents(){
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<OrderEventEntity> orderEvents = this.orderEventRepository.findAll(sort);
        log.info("Order events received: " + orderEvents.size());
        for (OrderEventEntity orderEvent : orderEvents) {
            this.publishEvent(orderEvent);
            orderEventRepository.delete(orderEvent);
        }
    }

    private void publishEvent(OrderEventEntity event){
        OrderEventType eventType = event.getEventType();
        if (Objects.requireNonNull(eventType) == OrderEventType.ORDER_CREATED) {
            OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
            this.orderEventPublisher.publish(orderCreatedEvent);
        } else {
            log.warn("Unknown event type: {}", eventType);
        }
    }

    private String toJsonPayload(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> clazz){
        try{
            return objectMapper.readValue(json, clazz);
        } catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

}

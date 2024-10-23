package com.cwen.order_service.jobs;

import com.cwen.order_service.domain.OrderEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsPublishingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublishingJob.class);

    private final OrderEventService orderEventService;

    public OrderEventsPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(fixedRate = 5000)
    public void publishOrderEvents() {
        log.info("Publishing order events");
        orderEventService.publishOrderEvents();
    }
}
package com.cwen.order_service.jobs;

import com.cwen.order_service.domain.OrderService;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class OrderProcessingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);

    private final OrderService orderService;

    OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 10000)
    @SchedulerLock(name = "processNewOrders")
    public void processNewOrders(){
        LockAssert.assertLocked();
        orderService.processNewOrders();
    }
}

package com.cwen.order_service.web.controllers;


import com.cwen.order_service.domain.OrderService;
import com.cwen.order_service.domain.SecurityService;
import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.CreateOrderResponse;
import com.cwen.order_service.domain.models.OrderDTO;
import com.cwen.order_service.domain.models.OrderSummary;
import com.cwen.order_service.web.exception.OrderNotFoundException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "security_auth")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest req){
        String username = securityService.getLoginUserName();
        log.info("Creating order for user: {}", username);
        return orderService.createOrder(username, req);
    }

    @GetMapping
    List<OrderSummary> getOrders(){
        String username = securityService.getLoginUserName();
        log.info("Retrieving orders for user: {}", username);
        return orderService.getOrders(username);
    }

    @GetMapping("/{orderNumber}")
    OrderDTO getOrder(@PathVariable String orderNumber){
        String username = securityService.getLoginUserName();
        log.info("Retrieving order: {} for user: {}", orderNumber,username);
        return orderService
                .getUserOrder(username,orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }

}

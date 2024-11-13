package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.orders.OrderServiceClient;
import com.cwen.bookstore_webapp.client.orders.models.CreateOrderRequest;
import com.cwen.bookstore_webapp.client.orders.models.OrderCreatedResponseDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderSummary;
import com.cwen.bookstore_webapp.services.SecurityHelper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderServiceClient orderServiceClient;
    private final SecurityHelper securityHelper;
    public OrderController(OrderServiceClient orderServiceClient, SecurityHelper securityHelper) {
        this.orderServiceClient = orderServiceClient;
        this.securityHelper = securityHelper;
    }

    @GetMapping("/cart")
    String cart(){
        return "cart";
    }

    @GetMapping("/orders/{orderNumber}")
    String orderDetails(@PathVariable String orderNumber, Model model){
        model.addAttribute("orderNumber", orderNumber);
        return "orderDetails";
    }

    @GetMapping("/orders")
    String ordersList(){
       return "orders";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderCreatedResponseDTO createOrder(@Valid @RequestBody CreateOrderRequest orderRequest){
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return orderServiceClient.createOrder(headers,orderRequest);
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getOrder(@PathVariable String orderNumber){
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return orderServiceClient.getOrder(headers,orderNumber);
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders(){
        String accessToken =  securityHelper.getAccessToken();
        Map<String, ?> headers = Map.of("Authorization", "Bearer " + accessToken);
        return orderServiceClient.getOrders(headers);
    }


}

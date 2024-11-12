package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.orders.OrderServiceClient;
import com.cwen.bookstore_webapp.client.orders.models.CreateOrderRequest;
import com.cwen.bookstore_webapp.client.orders.models.OrderCreatedResponseDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderSummary;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    private final OrderServiceClient orderServiceClient;
    public OrderController(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
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
        return orderServiceClient.createOrder(orderRequest);
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getORder(@PathVariable String orderNumber){
        return orderServiceClient.getOrder(orderNumber);
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders(){
        return orderServiceClient.getOrders();
    }


}

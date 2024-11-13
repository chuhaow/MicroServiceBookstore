package com.cwen.bookstore_webapp.client.orders;

import com.cwen.bookstore_webapp.client.orders.models.CreateOrderRequest;
import com.cwen.bookstore_webapp.client.orders.models.OrderCreatedResponseDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderDTO;
import com.cwen.bookstore_webapp.client.orders.models.OrderSummary;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Map;

public interface OrderServiceClient {
    @PostExchange("/orders/api/orders")
    OrderCreatedResponseDTO createOrder(
            @RequestHeader Map<String, ?> headers, @RequestBody CreateOrderRequest createOrderRequest);

    @GetExchange("/orders/api/orders")
    List<OrderSummary> getOrders(@RequestHeader Map<String, ?> headers);

    @GetExchange("/orders/api/orders/{orderNumber}")
    OrderDTO getOrder(
            @RequestHeader Map<String, ?> headers, @PathVariable String orderNumber);

}

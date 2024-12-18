package com.cwen.order_service.domain;

import com.cwen.order_service.clients.catalog.Models.Product;
import com.cwen.order_service.clients.catalog.ProductServiceClient;
import com.cwen.order_service.domain.exceptions.InvalidOrderException;
import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidator {
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient productServiceClient;

    public OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public void validate(CreateOrderRequest req){
        Set<OrderItem> items = req.items();
        for(OrderItem item : items){
            Product product = productServiceClient.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product Code "+ item.code()));
            if(product.price().compareTo(item.price()) != 0){
                log.error("Product price of {} was excepted but the item price was {}", product.price(),item.price());
                throw new InvalidOrderException("Product price does not match");
            }
        }
    }

}

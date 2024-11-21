package com.cwen.order_service.util;

import com.cwen.order_service.domain.models.Address;
import com.cwen.order_service.domain.models.CreateOrderRequest;
import com.cwen.order_service.domain.models.Customer;
import com.cwen.order_service.domain.models.OrderItem;
import org.instancio.Instancio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.instancio.Select.field;


public class TestDataFactory {
    static final Set<OrderItem> VALID_ORDER_ITEMS =  Set.of(new OrderItem("P100", "Product 1", new BigDecimal("34.00"), 1));
    static final List<String> VALID_COUNTRIES = List.of("Canada", "USA", "Japan");
    static final Set<OrderItem> INVALID_ORDER_BAD_CODE = Set.of(new OrderItem("ABC", "Product 1", new BigDecimal("34.00"), 1));
    static final Set<OrderItem> INVALID_ORDER_WRONG_PRICE = Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
    public static CreateOrderRequest createValidOrderRequest(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();

    }

    public static CreateOrderRequest createOrderRequestInvalidCustomer(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(Customer::phone), "")
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();

    }

    public static CreateOrderRequest createOrderRequestInvalidAddress(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .set(field(Address::country), "")
                .create();

    }

    public static CreateOrderRequest createOrderRequestBadProductCode(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(CreateOrderRequest::items), INVALID_ORDER_BAD_CODE)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();

    }

    public static CreateOrderRequest createOrderRequestWrongPrice(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(CreateOrderRequest::items), INVALID_ORDER_WRONG_PRICE)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();

    }

    public static CreateOrderRequest createOrderRequestNoItems() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), Set.of())
                .create();
    }
}

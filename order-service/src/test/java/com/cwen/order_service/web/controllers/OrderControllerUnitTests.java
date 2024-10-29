package com.cwen.order_service.web.controllers;

import com.cwen.order_service.domain.OrderService;
import com.cwen.order_service.domain.SecurityService;
import com.cwen.order_service.domain.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.cwen.order_service.util.TestDataFactory.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(OrderController.class)
public class OrderControllerUnitTests {
    @MockBean
    private OrderService orderService;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn("user");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void InvalidPayload(CreateOrderRequest req) throws Exception {
        given(orderService.createOrder(eq("user"), any(CreateOrderRequest.class)))
                .willReturn(null);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void getOrdersSuccess() throws Exception {
        List<OrderSummary> expectedOrders = Arrays.asList(
                new OrderSummary("asdf-1234", OrderStatus.NEW),
                new OrderSummary("fdsa-5678", OrderStatus.NEW)
        );
        when(securityService.getLoginUserName()).thenReturn("user");
        when(orderService.getOrders("user")).thenReturn(expectedOrders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOrders)));

    }

    @Test
    void getUserOrderSuccess() throws Exception {
        String orderNumber = "asdf-1234";
        String username = "user";
        OrderItem item = new OrderItem("P100", "item", new BigDecimal(34.00), 1);
        OrderDTO orderDTO = new OrderDTO(
                orderNumber,
                "user",
                Set.of(item),
                new Customer("john", "email", "1231231234"),
                new Address("123 street", "","City", "state", "1230033", "Canada"),
                OrderStatus.NEW,
                "Comment",
                LocalDateTime.now());

        when(securityService.getLoginUserName()).thenReturn(username);
        when(orderService.getUserOrder(username, orderNumber)).thenReturn(Optional.of(orderDTO));

        mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));
    }

    @Test
    void getOrderNotFound() throws Exception {
        String orderNumber = "non-existent-order";
        String username = "user";

        when(securityService.getLoginUserName()).thenReturn(username);
        when(orderService.getUserOrder(username, orderNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isNotFound());
    }


    public static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(named("Invalid Customer", createOrderRequestInvalidCustomer())),
                arguments(named("Invalid Address", createOrderRequestInvalidAddress())),
                arguments(named("No Items", createOrderRequestNoItems()))
        );
    }
}

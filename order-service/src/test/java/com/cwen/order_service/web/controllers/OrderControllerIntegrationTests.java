package com.cwen.order_service.web.controllers;

import com.cwen.order_service.AbstractIntegrationTest;
import com.cwen.order_service.domain.models.OrderSummary;
import com.cwen.order_service.util.TestDataFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@Sql("/test-orders.sql")
public class OrderControllerIntegrationTests extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {

        //The Bean exists, it's just a problem of with inspections
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Autowired
        private CircuitBreakerRegistry circuitBreakerRegistry;

        @BeforeEach
        public void setUp() {
            circuitBreakerRegistry.circuitBreaker("catalog-service").reset();
        }

        @Test
        void CreateOrderSuccessTest(){
            mockGetProductByCode("P100", "Product 1", new BigDecimal("34.00"));
           var payload = TestDataFactory.createValidOrderRequest();

           given().contentType(ContentType.JSON)
                   .body(payload)
                   .when()
                   .post("/api/orders")
                   .then()
                   .statusCode(HttpStatus.CREATED.value())
                   .body("orderNum", notNullValue());
        }

        @Test
        void CreateOrderInvalidItemTest(){
            mockGetProductByCodeNotFound("ABC", "Product 1", new BigDecimal("34.00"));
            var payload = TestDataFactory.createOrderRequestBadProductCode();

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void CreateOrderRequestBadCustomerData(){
            var payload = TestDataFactory.createOrderRequestInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", notNullValue());;
        }

        @Test
        void CreateOrderRequestBadAddressData(){
            var payload = TestDataFactory.createOrderRequestInvalidAddress();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", notNullValue());;
        }

        @Test
        void CreateOrderRequestNoItemData(){
            var payload = TestDataFactory.createOrderRequestNoItems();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", notNullValue());

        }
    }

    @Nested
    class GetOrderByOrderTests{
        @Test
        void shouldGetOrderSuccessTest(){
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>(){});
            assertThat(orderSummaries).hasSize(1);
        }
        //TODO: Update tests when user system is complete.
    }

    @Nested
    class GetOrderByOrderNumberTests{
        String orderNumber = "asdf-1234";

        @Test
        void shouldGetOrderSuccessTest(){
            given().when()
                    .get("/api/orders/" + orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is (orderNumber))
                    .body("items.size()", is(2));
        }
    }

}

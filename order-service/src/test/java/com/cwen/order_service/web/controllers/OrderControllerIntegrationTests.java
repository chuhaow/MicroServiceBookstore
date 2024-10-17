package com.cwen.order_service.web.controllers;

import com.cwen.order_service.AbstractIntegrationTest;
import com.cwen.order_service.util.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderControllerIntegrationTests extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {
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
        /*
        @Test
        void CreateOrderRequestBadItemCode(){
            var payload = TestDataFactory.createOrderRequestBadProductCode();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", notNullValue());

        }

        @Test
        void CreateOrderRequestWrongItemPrice(){
            var payload = TestDataFactory.createOrderRequestWrongPrice();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", notNullValue());

        }
         */
    }

}

package com.cwen.cart_service.controllers;

import com.cwen.cart_service.AbstractIntegrationTest;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GuestCartControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker("catalog-service").reset();
    }

    @Test
    void addToCartSuccessTest(){
        mockGetProductByCode("P100", "Product 1", new BigDecimal("34.00"));
        var payload = """
                    {
                        "guestId": "12345532454",
                        "item": {
                            "code": "P100",
                            "name": "Product 1",
                            "price": 34.00,
                            "quantity": 1
                        }
                    }""";

        given().contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("api/carts/guest")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("cartId", notNullValue());
    }
}

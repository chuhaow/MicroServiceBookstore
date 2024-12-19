package com.cwen.cart_service.controllers;

import com.cwen.cart_service.AbstractIntegrationTest;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
@Sql("/test-cart.sql")
public class CartControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker("catalog-service").reset();
    }

    /*
    @Nested
    class AddToCartTest{
        @Test
        void addToCartSuccessTest(){
            mockGetProductByCode("P100", "Product 1", new BigDecimal("34.00"));
            var payload = """
                    {
                        "item": {
                            "code": "P100",
                            "name": "Product 1",
                            "price": 34.00,
                            "quantity": 1
                        }
                    }""";

            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + getToken())
                    .body(payload)
                    .when()
                    .post("api/carts")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("cartId", notNullValue());
        }
    }
    */

    @Test
    void getCartSuccessTest(){
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getToken())
                .when()
                .get("api/carts")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteCartSuccessTest(){
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getToken())
                .when()
                .delete("api/carts")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Nested
    class UpdateCartTest{
        @Test
        void updateCartItemSuccessTest(){
            var payload = """
                    {
                        "itemCode": "P100",
                        "quantity": 5
                        
                    }""";
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + getToken())
                    .body(payload)
                    .when()
                    .post("api/carts/update/quantity")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("itemCode", notNullValue());

        }

        @Test
        void updateCartItemNotInCartTest(){
            var payload = """
                    {
                        "itemCode": "thisDoesNotExist",
                        "quantity": 5
                        
                    }""";
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + getToken())
                    .body(payload)
                    .when()
                    .post("api/carts/update/quantity")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }


}

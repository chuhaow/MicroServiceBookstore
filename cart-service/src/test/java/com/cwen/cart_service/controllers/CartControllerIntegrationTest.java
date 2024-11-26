package com.cwen.cart_service.controllers;

import com.cwen.cart_service.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
@Sql("/test-cart.sql")
public class CartControllerIntegrationTest extends AbstractIntegrationTest {

    @Nested
    class AddToCartTest{
        @Test
        void addToCartSuccessTest(){
            var payload = """
                    {
                        "userId": "user",
                        "item": {
                            "code": "P101",
                            "name": "A item",
                            "price": 30.30,
                            "quantity": 1
                        }
                    }""";

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("api/carts")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("cartId", notNullValue());
        }
    }

    @Test
    void getCartSuccessTest(){
        given().contentType(ContentType.JSON)
                .when()
                .get("api/carts")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteCartSuccessTest(){
        given().contentType(ContentType.JSON)
                .when()
                .delete("api/carts")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}

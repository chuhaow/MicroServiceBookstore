package com.cwen.cart_service.controllers;

import com.cwen.cart_service.AbstractIntegrationTest;
import com.cwen.cart_service.domain.entities.guest.GuestCartEntity;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
@SqlConfig(dataSource = "testGuestDataSource", transactionManager = "testGuestTransactionManager")
@Sql("/test-guest-cart.sql")
public class GuestCartControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker("catalog-service").reset();
    }

    @Nested
    class GetGuestCartTests{
        @Test
        void getGuestCartSuccessTest(){
            String guestId = "abcd12344321";
            given().contentType(ContentType.JSON)
                    .when()
                    .get("api/carts/guest/" + guestId)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void getGuestCartIdNotFoundTest(){
            String guestId = "ThisUserDoesNotExist";
            given().contentType(ContentType.JSON)
                    .when()
                    .get("api/carts/guest/" + guestId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(equalTo("[]"));
        }

    }

    @Nested
    class UpdateGuestCartTest{
        @Test
        void updateGuestCartItemSuccessTest(){
            var payload = """
                    {
                        "guestId": "abcd12344321",
                        "itemCode": "P100",
                        "quantity": 5
                        
                    }""";
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("api/carts/guest/update/quantity")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("itemCode", notNullValue());

        }

        @Test
        void updateCartItemNotInCartTest(){
            var payload = """
                    {
                        "guestId": "abcd12344321",
                        "itemCode": "thisDoesNotExist",
                        "quantity": 5
                        
                    }""";
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("api/carts/guest/update/quantity")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }




}

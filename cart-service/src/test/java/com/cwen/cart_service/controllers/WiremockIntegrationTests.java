package com.cwen.cart_service.controllers;

import com.cwen.cart_service.AbstractIntegrationTest;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Container;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class WiremockIntegrationTests extends AbstractIntegrationTest {
    @Container
    public static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine").withReuse(true);

    @BeforeAll
    static void beforeAll(){
        if (!wiremockServer.isRunning()) {
            wiremockServer.start();
        }
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
        System.out.println("InBeforeAll:" + wiremockServer.getBaseUrl());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("InAfterAll:" + wiremockServer.getBaseUrl());
        wiremockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cart.catalog-service-url", wiremockServer::getBaseUrl);
    }


    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker("catalog-service").reset();
    }


    @Nested
    class AuthCartTest{
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

    @Nested
    class GuestCartTest{
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

    protected static void mockGetProductByCode(String code, String name, BigDecimal price){
        stubFor(WireMock.get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                        {
                           "code":"%s",
                           "name":"%s",
                           "description":"Winning will make you famous. Losing means certain death...",
                           "imageURL":"https://images.gr-assets.com/books/1447303603l/2767052.jpg",
                           "price":%f
                        }
                        """.formatted(code,name, price.doubleValue()))));
    }



}

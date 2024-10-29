package com.cwen.order_service;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    public static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");

    @BeforeAll
    static void beforeAll(){
        wiremockServer.start();
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());

    }

    @AfterAll
    static void afterAll() {
        wiremockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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

    protected static void mockGetProductByCodeNotFound(String code, String name, BigDecimal price){
        stubFor(WireMock.get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(400)
                        .withBody("""
                                {
                                    "type": "https://api.bookstore.com/errors/bad-request",
                                    "title": "Invalid Order",
                                    "status": 400,
                                    "detail": "Cannot find product with code: abc",
                                    "instance": "/api/products/abc",
                                    "service": "catalog-service",
                                    "error_category": "Generic",
                                    "timestamp": "%s"
                                }
                        """.formatted(Instant.now()))));
    }
}

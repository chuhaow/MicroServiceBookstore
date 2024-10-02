package com.cwen.catalog_service.web.controllers;

import com.cwen.catalog_service.AbstractIntergrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/test-data.sql")
public class ProductControllerTest extends AbstractIntergrationTest {

    @Test
    public void shouldReturnProducts(){
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(15))
                .body("totalPages", is(2))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrev", is(false));
    }
}
package com.cwen.order_service.web.controllers;

import com.cwen.order_service.AbstractIntegrationTest;
import com.cwen.order_service.util.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderControllerIntegrationTests extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {
        @Test
        void CreateOrderSuccessTest(){
           var payload = """
                   {
                       "customer":{
                           "name": "John",
                           "email" : "siva@gmail.com",
                           "phone": "111-1111-1111"
                       },
                       "address" : {
                           "addressLine1" : "111 Street st.",
                           "addressLine2" : " ",
                           "city" : "City",
                           "state" : "State",
                           "zipCode": "0000000",
                           "country" : "Country"
                       },
                       "items" : [
                           {
                               "code" : "P100",
                               "name" : "Product 1",
                               "price" : 25.50,
                               "quantity" : 1
                           }
                       ]
                   }""";

           given().contentType(ContentType.JSON)
                   .body(payload)
                   .when()
                   .post("/api/orders")
                   .then()
                   .statusCode(HttpStatus.CREATED.value())
                   .body("orderNum", notNullValue());
        }

        @Test
        void CreateOrderRequestMissMandatoryData(){
            var payload = TestDataFactory.createOrderRequestInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

}

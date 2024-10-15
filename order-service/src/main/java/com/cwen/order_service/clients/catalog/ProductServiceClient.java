package com.cwen.order_service.clients.catalog;

import com.cwen.order_service.clients.catalog.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;


@Component
public class ProductServiceClient {
    private static Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<Product> getProductByCode(String code){
        log.info("Fetching Product for code: {}", code);
        try{
            var product = restClient
                    .get()
                    .uri("/api/products/{code}", code)
                    .retrieve()
                    .body(Product.class);
            return Optional.ofNullable(product);
        }catch(Exception e){
            log.error("Error fetching product: {}", e.getMessage());
            return Optional.empty();
        }


    }
}

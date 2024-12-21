package com.cwen.cart_service.catalog;


import com.cwen.cart_service.catalog.Models.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
public class ProductServiceClient {
    private static Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestTemplate restTemplate;

    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code){
        log.info("Fetching Product for code: {}", code);
        try {
            Product product = restTemplate.getForObject("/api/products/{code}", Product.class, code);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            log.error("Error while fetching product for code {}: {}", code, e.getMessage());
            return Optional.empty();
        }
    }

    Optional<Product> getProductByCodeFallback(String code, Throwable t){
        log.error("Error fetching product: {}. {}", code, t.getMessage());
        return Optional.empty();
    }
}

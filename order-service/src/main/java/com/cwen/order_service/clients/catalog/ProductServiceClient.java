package com.cwen.order_service.clients.catalog;

import com.cwen.order_service.clients.catalog.Models.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;


@Component
public class ProductServiceClient {
    private static Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;
    private final String catalogServiceBaseUrl = System.getenv("ORDER_CATALOG_SERVICE_URL");

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code){
        log.info("Fetching Product for code: {}", code);

        var product = restClient
                .get()
                .uri("/api/products/{code}", code)
                .retrieve()
                .body(Product.class);
        return Optional.ofNullable(product);
    }

    Optional<Product> getProductByCodeFallback(String code, Throwable t){
        log.error("Error fetching product: {}. {}", code, t.getMessage());
        return Optional.empty();
    }
}

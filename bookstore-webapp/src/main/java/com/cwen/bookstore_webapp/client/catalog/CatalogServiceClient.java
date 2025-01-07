package com.cwen.bookstore_webapp.client.catalog;

import com.cwen.bookstore_webapp.client.catalog.models.PagedResult;
import com.cwen.bookstore_webapp.client.catalog.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface CatalogServiceClient {
    @GetExchange("/catalog/api/products")
    PagedResult<Product> getProduct(@RequestParam int page);

    @GetExchange("/catalog/api/products/{code}")
    ResponseEntity<Product> getProductByCode(@RequestParam String code);
}

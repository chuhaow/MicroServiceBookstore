package com.cwen.catalog_service.web.controllers;

import com.cwen.catalog_service.domain.PagedResult;
import com.cwen.catalog_service.domain.Product;
import com.cwen.catalog_service.domain.ProductNotFoundException;
import com.cwen.catalog_service.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
class ProductController {
    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue ="1") int pageNum){
        return productService.getProducts(pageNum);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code){
        return productService.getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.create("Cannot find product with code: " + code));
    }
}

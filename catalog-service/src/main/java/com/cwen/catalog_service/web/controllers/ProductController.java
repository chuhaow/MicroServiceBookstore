package com.cwen.catalog_service.web.controllers;

import com.cwen.catalog_service.domain.PagedResult;
import com.cwen.catalog_service.domain.Product;
import com.cwen.catalog_service.domain.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

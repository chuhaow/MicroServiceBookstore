package com.cwen.bookstore_webapp.web.controller;

import com.cwen.bookstore_webapp.client.catalog.CatalogServiceClient;
import com.cwen.bookstore_webapp.client.catalog.models.PagedResult;
import com.cwen.bookstore_webapp.client.catalog.models.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final CatalogServiceClient catalogServiceClient;

    ProductController(CatalogServiceClient catalogServiceClient) {
        this.catalogServiceClient = catalogServiceClient;
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String productsPage(@RequestParam(name = "page", defaultValue = "1")int pageNo,
                        Model model){
        model.addAttribute("pageNo", pageNo);
        return "products";
    }

    @GetMapping("/api/products")
    @ResponseBody
    PagedResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model){
        return catalogServiceClient.getProduct(pageNo);
    }
}

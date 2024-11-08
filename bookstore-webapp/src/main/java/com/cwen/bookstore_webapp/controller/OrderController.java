package com.cwen.bookstore_webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

    @GetMapping("/cart")
    String cart(){
        return "cart";
    }

    @GetMapping("/orders/{orderNumber}")
    String orderDetails(@PathVariable String orderNumber, Model model){
        model.addAttribute("orderNumber", orderNumber);
        return "orderDetails";
    }
    @GetMapping("/orders")
    String ordersList(){
       return "orders";
    }
}

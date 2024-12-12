package com.cwen.bookstore_webapp.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewController {


    @Value("${REALM_URL}")
    private String realmUrl;

    @Value("${spring.security.oauth2.client.registration.bookstore-webapp.redirect-uri}")
    private String redirectUri;

    @GetMapping("/")
    public String showHomePage(RedirectAttributes redirectAttributes) {
        String registrationUrl = realmUrl + "/protocol/openid-connect/registrations?client_id=bookstore-webapp&scope=openid%20profile&redirect_uri="+
                redirectUri+"&response_type=code";
        redirectAttributes.addFlashAttribute("registrationUrl", registrationUrl);
        System.out.println(registrationUrl);
        return "redirect:/products";
    }
}

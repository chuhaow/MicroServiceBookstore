package com.cwen.bookstore_webapp.web.controller.advisor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalAttributeAdvisor {
    @Value("${REALM_URL}")
    private String realmUrl;

    @Value("${spring.security.oauth2.client.registration.bookstore-webapp.redirect-uri}")
    private String redirectUri;

    @ModelAttribute("registrationUrl")
    public String getRegistrationUrl() {
        return realmUrl + "/protocol/openid-connect/registrations?client_id=bookstore-webapp&scope=openid%20profile&redirect_uri="+
                redirectUri+"&response_type=code";
    }
}

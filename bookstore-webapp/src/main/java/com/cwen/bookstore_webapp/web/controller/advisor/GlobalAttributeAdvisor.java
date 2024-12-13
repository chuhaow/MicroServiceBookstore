package com.cwen.bookstore_webapp.web.controller.advisor;

import com.cwen.bookstore_webapp.services.SecurityHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalAttributeAdvisor {
    private final SecurityHelper securityHelper;

    GlobalAttributeAdvisor(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @Value("${REALM_URL}")
    private String realmUrl;

    @Value("${spring.security.oauth2.client.registration.bookstore-webapp.redirect-uri}")
    private String redirectUri;

    @ModelAttribute("registrationUrl")
    public String getRegistrationUrl() {
        return realmUrl + "/protocol/openid-connect/registrations?client_id=bookstore-webapp&scope=openid%20profile&redirect_uri="+
                redirectUri+"&response_type=code";
    }

    @ModelAttribute("username")
    public String getUsername() {
        return securityHelper.getLoginUsername() == null ? "Guest" :securityHelper.getLoginUsername();
    }

    @ModelAttribute("userEmail")
    public String getEmail() {
        return securityHelper.getLoginEmail() == null ? "Placeholder@Email.com" :securityHelper.getLoginEmail();
    }
}

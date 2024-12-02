package com.cwen.cart_service.domain;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUsername(){
        JwtAuthenticationToken authenticationToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        return jwt.getClaimAsString("preferred_username");
    }


}

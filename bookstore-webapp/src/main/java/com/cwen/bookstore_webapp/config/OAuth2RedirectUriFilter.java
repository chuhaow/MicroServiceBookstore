package com.cwen.bookstore_webapp.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OAuth2RedirectUriFilter extends OncePerRequestFilter {
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (requestUri.contains("/login/oauth2/code/") && request.getParameter("code") == null) {
            response.sendRedirect("/oauth2/authorization/bookstore-webapp");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
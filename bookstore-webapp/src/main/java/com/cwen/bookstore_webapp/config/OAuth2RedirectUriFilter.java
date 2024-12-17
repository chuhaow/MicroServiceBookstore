package com.cwen.bookstore_webapp.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OAuth2RedirectUriFilter extends OncePerRequestFilter {

    private final String loginUrl;
    private final String authorizeUrl;

    public OAuth2RedirectUriFilter(String loginUrl, String authorizeUrl) {
        this.loginUrl = loginUrl;
        this.authorizeUrl = authorizeUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (requestUri.contains(loginUrl) && request.getParameter("code") == null) {
            response.sendRedirect(authorizeUrl);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
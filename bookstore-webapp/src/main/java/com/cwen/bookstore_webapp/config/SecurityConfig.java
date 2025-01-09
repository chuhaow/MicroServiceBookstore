package com.cwen.bookstore_webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${security.oauth2.login-url}")
    private String loginUrl;

    @Value("${security.oauth2.authorization-uri}")
    private String authorizeUrl;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c.requestMatchers(
                                "/js/*",
                                "/css/*",
                                "/images/*",
                                "/error",
                                "/",
                                "/cart",
                                "/actuator/**",
                                "/products/**",
                                "/api/products/**",
                                "/api/carts/guest/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(CorsConfigurer::disable) //TODO: Figure out something better than disabling these
                .csrf(CsrfConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler((request, response, exception) -> {
                            // Redirect to login page on failure
                            response.sendRedirect(loginUrl);
                        })
                )
                .logout(logout -> logout.clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(logoutSuccessHandler()));

        http.addFilterBefore(new OAuth2RedirectUriFilter(loginUrl,authorizeUrl), OAuth2AuthorizationRequestRedirectFilter.class);
        return http.build();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidcClientInitiatedLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcClientInitiatedLogoutSuccessHandler;
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

                String preferredUsername = (String) oauth2AuthenticationToken.getPrincipal()
                        .getAttributes().get("preferred_username");

                System.out.println("Login successful for user (preferred username): " + preferredUsername);
                response.sendRedirect("/products?merge=" + true);
            } else {

                String username = authentication.getName();
                System.out.println("Login successful for user: " + username);
                response.sendRedirect("/");
            }

        };
    }

}

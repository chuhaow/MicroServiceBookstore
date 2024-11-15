package com.cwen.bookstore_webapp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityHelper {
    private final OAuth2AuthorizedClientService oauth2AuthorizedClientService;

    public SecurityHelper(OAuth2AuthorizedClientService oauth2AuthorizedClientService) {
        this.oauth2AuthorizedClientService = oauth2AuthorizedClientService;
    }

    public String getAccessToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken)){
            return null;
        }
        OAuth2AuthorizedClient authorizedClient = oauth2AuthorizedClientService.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        return authorizedClient.getAccessToken().getTokenValue();
    }
}
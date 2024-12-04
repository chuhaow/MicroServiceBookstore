package com.cwen.cart_service;

import com.cwen.cart_service.model.KeyCloakToken;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {
    private static final String CLIENT_ID = "bookstore-webapp";
    private static final String CLIENT_SECRET = "YMtmsNEPbq6v5jLZzkRi72hhAomOsVQ0";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(OAuth2Constants.GRANT_TYPE, singletonList(OAuth2Constants.PASSWORD));
        map.put(OAuth2Constants.CLIENT_ID, singletonList(CLIENT_ID));
        map.put(OAuth2Constants.CLIENT_SECRET, singletonList(CLIENT_SECRET));
        map.put(OAuth2Constants.USERNAME, singletonList(USERNAME));
        map.put(OAuth2Constants.PASSWORD, singletonList(PASSWORD));

        String authServerUrl =
                oAuth2ResourceServerProperties.getJwt().getIssuerUri() + "/protocol/openid-connect/token";

        var request = new HttpEntity<>(map, httpHeaders);
        KeyCloakToken token = restTemplate.postForObject(authServerUrl, request, KeyCloakToken.class);

        assert token != null;
        return token.accessToken();
    }
}

package com.cwen.cart_service;

import com.cwen.cart_service.model.KeyCloakToken;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.singletonList;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {
    private static final String CLIENT_ID = "bookstore-webapp";
    private static final String CLIENT_SECRET = "YMtmsNEPbq6v5jLZzkRi72hhAomOsVQ0";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @LocalServerPort
    private int port;

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;


    public static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine").withReuse(true);

    @BeforeAll
    static void beforeAll(){
        if (!wiremockServer.isRunning()) {
            wiremockServer.start();
        }
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
        System.out.println("InBeforeAll:" + wiremockServer.getBaseUrl());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("InAfterAll:" + wiremockServer.getBaseUrl());
        wiremockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cart.catalog-service-url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        String baseUrl = wiremockServer.getBaseUrl();
        applicationProperties.setCatalogServiceUrl(baseUrl);


        restTemplate = new RestTemplateBuilder()
                .rootUri(applicationProperties.catalogServiceUrl())
                .build();
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price){
        stubFor(WireMock.get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                        {
                           "code":"%s",
                           "name":"%s",
                           "description":"Winning will make you famous. Losing means certain death...",
                           "imageURL":"https://images.gr-assets.com/books/1447303603l/2767052.jpg",
                           "price":%f
                        }
                        """.formatted(code,name, price.doubleValue()))));
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

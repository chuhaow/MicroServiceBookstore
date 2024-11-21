package com.cwen.order_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${swagger.api-gateway-url}")
    private String apiGatewayUrl;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service")
                        .description("Order Service API")
                        .version("1.0")
                        .contact(new Contact().name("cwen").email("cwen@cwen.com")))
                .servers(List.of(new Server().url(apiGatewayUrl)))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes("security_auth",
                                new SecurityScheme()
                                        .in(SecurityScheme.In.HEADER)
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                                        .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                                        .scopes(new Scopes().addString("openid", "openid scope"))))));
    }

}

package com.cwen.cart_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {


    @Value("${swagger.api-gateway-url}")
    private String apiGatewayUrl;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart Service")
                        .description("Cart Service")
                        .version("1.0")
                        .contact(new Contact().name("cwen").email("cwen@gmail.com")))
                .servers(List.of(new Server().url(apiGatewayUrl)));
    }

}

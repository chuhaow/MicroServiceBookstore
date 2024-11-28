package com.cwen.bookstore_webapp.client;

import com.cwen.bookstore_webapp.ApplicationProperties;
import com.cwen.bookstore_webapp.client.cart.CartServiceClient;
import com.cwen.bookstore_webapp.client.catalog.CatalogServiceClient;
import com.cwen.bookstore_webapp.client.orders.OrderServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientsConfig {
    private final ApplicationProperties applicationProperties;

    ClientsConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    CatalogServiceClient catalogServiceClient() {
        RestClient restClient = RestClient.create(applicationProperties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(CatalogServiceClient.class);
    }

    @Bean
    OrderServiceClient orderServiceClient() {
        RestClient restClient = RestClient.create(applicationProperties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(OrderServiceClient.class);
    }

    @Bean
    CartServiceClient cartServiceClient() {
        RestClient restClient = RestClient.create(applicationProperties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(CartServiceClient.class);
    }
}

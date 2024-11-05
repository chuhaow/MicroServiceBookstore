package com.cwen.api_gateway;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

@Configuration
public class SwaggerConfig {
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    public SwaggerConfig(RouteDefinitionLocator routeDefinitionLocator, SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
    }

    @PostConstruct
    public void init(){
        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        Objects.requireNonNull(definitions).stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .forEach(routeDefinition -> {
                    String serviceName = routeDefinition.getId().replaceAll("-service", "");
                    AbstractSwaggerUiConfigProperties.SwaggerUrl url =
                            new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, DEFAULT_API_DOCS_URL + "/" + serviceName, null);
                    urls.add(url);
                });

        swaggerUiConfigProperties.setUrls(urls);
    }


}

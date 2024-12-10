package com.cwen.cart_service;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
	private static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.0.5";
	private static String realmImportFile = "/test-bookstore-realm.json";
	private static String realmName = "bookstore";

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		System.out.println("Creating Test container");
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"));
	}

	@Bean
	KeycloakContainer keycloak(DynamicPropertyRegistry registry){
		var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
				.withRealmImportFile(realmImportFile)
				.withStartupTimeout(java.time.Duration.ofMinutes(10));
		registry.add(
				"spring.security.oauth2.resourceserver.jwt.issuer-uri",
				() -> keycloak.getAuthServerUrl() + "/realms/" + realmName);
		return keycloak;
	}
}

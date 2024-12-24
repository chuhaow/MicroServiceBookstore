package com.cwen.cart_service;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.HashMap;

@TestConfiguration(proxyBeanMethods = true)
class TestcontainersConfiguration {
	private static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.0.5";
	private static String realmImportFile = "/test-bookstore-realm.json";
	private static String realmName = "bookstore";


	@Bean
	@ServiceConnection
    static PostgreSQLContainer<?> authPostgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
				.withDatabaseName("authdb")
				.withUsername("authuser")
				.withPassword("authpass");
	}

	@Bean
	@ServiceConnection
	static PostgreSQLContainer<?> guestPostgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
				.withDatabaseName("guestdb")
				.withUsername("guestuser")
				.withPassword("guestpass");
	}

	@DynamicPropertySource
	static void configureDynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.auth.url", () -> authPostgresContainer().getJdbcUrl());
		registry.add("spring.datasource.auth.username", () -> authPostgresContainer().getUsername());
		registry.add("spring.datasource.auth.password", () -> authPostgresContainer().getPassword());

		registry.add("spring.datasource.guest.url", () -> guestPostgresContainer().getJdbcUrl());
		registry.add("spring.datasource.guest.username", () -> guestPostgresContainer().getUsername());
		registry.add("spring.datasource.guest.password", () -> guestPostgresContainer().getPassword());
	}

	@Bean
	KeycloakContainer keycloak(DynamicPropertyRegistry registry) {
		var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
				.withRealmImportFile(realmImportFile)
				.withStartupTimeout(java.time.Duration.ofMinutes(10));

		registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
				() -> keycloak.getAuthServerUrl() + "/realms/" + realmName);
		return keycloak;
	}

	@Bean(name = "testAuthDataSource")
	@Primary
	DataSource authDataSource(PostgreSQLContainer<?> authPostgresContainer) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(authPostgresContainer.getJdbcUrl());
		dataSource.setUsername(authPostgresContainer.getUsername());
		dataSource.setPassword(authPostgresContainer.getPassword());
		return dataSource;
	}

	@Bean(name = "testGuestDataSource")
	DataSource guestDataSource(PostgreSQLContainer<?> guestPostgresContainer) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(guestPostgresContainer.getJdbcUrl());
		dataSource.setUsername(guestPostgresContainer.getUsername());
		dataSource.setPassword(guestPostgresContainer.getPassword());
		return dataSource;
	}


	@Primary
	@Bean(name = {"testAuthUserEntityManagerFactory"})
	public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(EntityManagerFactoryBuilder builder,
																		   @Qualifier("testAuthDataSource") DataSource authDataSource) {
		return builder
				.dataSource(authDataSource)
				.packages("com.cwen.cart_service.domain")  // Replace with your entity package
				.persistenceUnit("authPU")
				.build();
	}


	@Bean(name = "testGuestEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean guestEntityManagerFactory(EntityManagerFactoryBuilder builder,
																			@Qualifier("testGuestDataSource") DataSource guestDataSource) {
		return builder
				.dataSource(guestDataSource)
				.packages("com.cwen.cart_service.domain")  // Replace with your entity package
				.persistenceUnit("guestPU")
				.build();
	}


	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}
}

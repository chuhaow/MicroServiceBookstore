package com.cwen.cart_service;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.HashMap;
//TODO: Tests run very slowly locally. Improve this. Keycloak does not need to start for Guest Tests
@TestConfiguration(proxyBeanMethods = true)
class TestcontainersConfiguration {
	private static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.0.5";
	private static String realmImportFile = "/test-bookstore-realm.json";
	private static String realmName = "bookstore";


	@Bean
	@ServiceConnection
	static PostgreSQLContainer<?> authPostgresContainer() {
		return AuthContainerHolder.INSTANCE;
	}

	@Bean
	@ServiceConnection
	static PostgreSQLContainer<?> guestPostgresContainer() {
		return GuestContainerHolder.INSTANCE;
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
		System.out.println("Guest DB URL: " + guestPostgresContainer.getJdbcUrl());
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
				.packages("com.cwen.cart_service.domain.entities.auth", "com.cwen.cart_service.domain.entities")
				.persistenceUnit("authPU")
				.build();
	}


	@Bean(name = "testGuestEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean guestEntityManagerFactory(EntityManagerFactoryBuilder builder,
																			@Qualifier("testGuestDataSource") DataSource guestDataSource) {
		return builder
				.dataSource(guestDataSource)
				.packages("com.cwen.cart_service.domain.entities.guest", "com.cwen.cart_service.domain.entities")
				.persistenceUnit("guestPU")
				.build();
	}

	@Bean(name = "testAuthFlyway")
	public Flyway authFlyway(@Qualifier("testAuthDataSource") DataSource dataSource, DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.auth.jdbcUrl", () -> authPostgresContainer().getJdbcUrl());
		registry.add("spring.datasource.auth.username", () -> authPostgresContainer().getUsername());
		registry.add("spring.datasource.auth.password", () -> authPostgresContainer().getPassword());
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.locations("classpath:db/migration/auth")
				.load();
		flyway.migrate();
		return flyway;
	}

	@Bean(name = "guestAuthFlyway")
	public Flyway guestFlyway(@Qualifier("testGuestDataSource") DataSource dataSource, DynamicPropertyRegistry registry) {
		System.out.println(guestPostgresContainer().getJdbcUrl());
		System.out.println(guestPostgresContainer().getUsername());
		System.out.println(guestPostgresContainer().getPassword());
		registry.add("spring.datasource.guest.jdbcUrl", () -> guestPostgresContainer().getJdbcUrl());
		registry.add("spring.datasource.guest.username", () -> guestPostgresContainer().getUsername());
		registry.add("spring.datasource.guest.password", () -> guestPostgresContainer().getPassword());
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.locations("classpath:db/migration/guest")
				.load();
		flyway.migrate();
		return flyway;
	}

	@Bean(name = "testGuestTransactionManager")
	public PlatformTransactionManager guestTransactionManager(
			@Qualifier("testGuestEntityManagerFactory") EntityManagerFactory guestEntityManagerFactory) {
		return new JpaTransactionManager(guestEntityManagerFactory);
	}


	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	private static class GuestContainerHolder {
		static final PostgreSQLContainer<?> INSTANCE = createAndStartContainer();

		private static PostgreSQLContainer<?> createAndStartContainer() {
			PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
					.withDatabaseName("guestdb")
					.withUsername("guestuser")
					.withPassword("guestpass");
			container.start();
			return container;
		}
	}

	private static class AuthContainerHolder {
		static final PostgreSQLContainer<?> INSTANCE = createAndStartContainer();

		private static PostgreSQLContainer<?> createAndStartContainer() {
			PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
					.withDatabaseName("authdb")
					.withUsername("authuser")
					.withPassword("authpass");
			container.start();
			return container;
		}
	}

}

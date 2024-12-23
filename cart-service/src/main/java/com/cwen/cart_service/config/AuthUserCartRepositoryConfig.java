package com.cwen.cart_service.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "com.cwen.cart_service.domain.repositories",
        entityManagerFactoryRef = "authUserEntityManagerFactory",
        transactionManagerRef = "authUserEntityManagerFactory"
)
public class AuthUserCartRepositoryConfig {
}

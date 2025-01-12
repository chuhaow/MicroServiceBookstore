package com.cwen.cart_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.cwen.cart_service.domain.repositories.guest",
        entityManagerFactoryRef = "guestUserEntityManagerFactory",
        transactionManagerRef = "guestTransactionManager"
)
public class GuestCartRepositoryConfig {
}

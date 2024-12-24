package com.cwen.cart_service.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class EntityManagerConfig {

    @Primary
    @Bean(name = {"authUserEntityManagerFactory", "entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean authUserEntityManagerFactory(
            @Qualifier("authDataSource")DataSource userDataSource, EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(userDataSource)
                .packages("com.cwen.cart_service.domain")
                .persistenceUnit("authUserPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "authUserTransactionManager")
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("authUserEntityManagerFactory") EntityManagerFactory userEntityManagerFactory) {
        return new JpaTransactionManager(userEntityManagerFactory);
    }

    @Bean(name = "guestUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean guestUserEntityManagerFactory(
            @Qualifier("guestDataSource") DataSource guestDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(guestDataSource)
                .packages("com.cwen.cart_service.domain")
                .persistenceUnit("guestUserPersistenceUnit")
                .build();
    }

    @Bean(name = "guestTransactionManager")
    public PlatformTransactionManager guestTransactionManager(
            @Qualifier("guestUserEntityManagerFactory") EntityManagerFactory guestEntityManagerFactory) {
        return new JpaTransactionManager(guestEntityManagerFactory);
    }
}

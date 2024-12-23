package com.cwen.cart_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "authDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:45432/postgres")
                .username("postgres")
                .password("password")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "guestDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.guest")
    public DataSource guestDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:55432/postgres")
                .username("postgres")
                .password("password")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

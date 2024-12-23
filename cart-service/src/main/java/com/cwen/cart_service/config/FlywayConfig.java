package com.cwen.cart_service.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {
    @Bean
    public Flyway authFlyway(@Qualifier("authDataSource") DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/auth")
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public Flyway guestFlyway(@Qualifier("guestDataSource") DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/guest")
                .load();
        flyway.migrate();
        return flyway;
    }
}

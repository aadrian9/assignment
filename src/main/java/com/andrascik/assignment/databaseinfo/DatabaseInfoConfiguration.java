package com.andrascik.assignment.databaseinfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInfoConfiguration {
    @Bean
    public PostgreSqlConnectionFactory postgreSqlConnectionFactory() {
        return new PostgreSqlConnection.Factory();
    }
}

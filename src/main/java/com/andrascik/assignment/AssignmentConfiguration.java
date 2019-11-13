package com.andrascik.assignment;

import com.andrascik.assignment.databaseinfo.PostgreSqlConnection;
import com.andrascik.assignment.databaseinfo.PostgreSqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssignmentConfiguration {
    @Bean
    public PostgreSqlConnectionFactory postgreSqlConnectionFactory() {
        return new PostgreSqlConnection.Factory();
    }
}

package com.andrascik.assignment.databaseinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PostgreSqlRequestTest {
    private PostgreSqlRequest request;
    private PostgreSqlConnection connection;

    @BeforeEach
    void setUp() {
        connection = mock(PostgreSqlConnection.class);
        request = new PostgreSqlRequest(connection);
    }


    @Test
    void createConnection_urlBuiltCorrectly() {

    }
}
package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.PostgreSqlConnection;
import com.andrascik.assignment.databaseinfo.PostgreSqlConnectionFactory;

public class MockedConnectionFactory implements PostgreSqlConnectionFactory {
    private final PostgreSqlConnection mockConnection;

    public MockedConnectionFactory(PostgreSqlConnection mockConnection) {
        this.mockConnection = mockConnection;
    }

    @Override
    public PostgreSqlConnection create(String hostName, Integer port, String databaseName, String userName, String password) {
        return mockConnection;
    }
}

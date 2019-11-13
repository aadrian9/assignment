package com.andrascik.assignment.databaseinfo;

import java.sql.*;

/**
 * Wrapper for {@link Connection} specific to PostgreSQL database.
 */
public class PostgreSqlConnection implements AutoCloseable {
    private static final String POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";

    private final Connection connection;

    private PostgreSqlConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Query for database meta data.
     * @return
     */
    public DatabaseMetaData getMetaData() {
        try {
            return connection.getMetaData();
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    /**
     * Prepare a database query.
     * @param statement
     * @return
     */
    public PreparedStatement prepareStatement(String statement) {
        try {
            return connection.prepareStatement(statement);
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    public static class Factory implements PostgreSqlConnectionFactory {
        @Override
        public PostgreSqlConnection create(
                String hostName, Integer port, String databaseName, String userName, String password) {
            final String connectionUrl = POSTGRESQL_URL_PREFIX + hostName + ":" + port + "/" + databaseName;
            try {
                return new PostgreSqlConnection(DriverManager.getConnection(connectionUrl, userName, password));
            } catch (SQLException e) {
                throw new PostgreSqlConnectionException(e);
            }
        }
    }
}

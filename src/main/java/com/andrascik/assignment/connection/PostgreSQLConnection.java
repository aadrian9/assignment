package com.andrascik.assignment.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLConnection {
    private static final String POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";
    private static final String SCHEMAS_COLUMN_NAME = "schema_name";
    private static final String LIST_SCHEMAS_QUERY = "SELECT " + SCHEMAS_COLUMN_NAME + " FROM information_schema.schemata;";
    private static final String TABLES_COLUMN_NAME = "table_name";
    private static final String LIST_TABLES_QUERY = "SELECT " + TABLES_COLUMN_NAME
            + " FROM information_schema.tables WHERE table_schema = '?';";
    private static final String COLUMNS_COLUMN_NAME = "table_name";
    private static final String LIST_COLUMNS_QUERY = "SELECT " + COLUMNS_COLUMN_NAME
            + " FROM information_schema.columns WHERE table_schema = '?' AND table_name = '?';";


    private final String connectionUrl;
    private final String userName;
    private final String password;

    private PostgreSQLConnection(String connectionUrl, String userName, String password) {
        this.connectionUrl = connectionUrl;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Lists all schemas for this database connection.
     * @return
     */
    public List<String> listSchemas() {
        final List<String> schemas = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             final Statement statement = connection.createStatement()
        ) {
            final ResultSet resultSet = statement.executeQuery(LIST_SCHEMAS_QUERY);
            while (resultSet.next()) {
                schemas.add(resultSet.getString(SCHEMAS_COLUMN_NAME));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return schemas;
    }

    /**
     * Lists all tables in specified schema for this database connection.
     * @param schema
     * @return
     */
    public List<String> listTables(String schema) {
        final List<String> tables = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             final PreparedStatement statement = connection.prepareStatement(LIST_TABLES_QUERY)
        ) {
            statement.setString(1, schema);
            final ResultSet resultSet = statement.executeQuery(LIST_TABLES_QUERY);
            while (resultSet.next()) {
                tables.add(resultSet.getString(TABLES_COLUMN_NAME));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return tables;
    }

    /**
     * Lists all tables in specified schema and table for this database connection.
     * @param schema
     * @param table
     * @return
     */
    public List<String> listColumns(String schema, String table) {
        final List<String> columns = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             final PreparedStatement statement = connection.prepareStatement(LIST_TABLES_QUERY)
        ) {
            statement.setString(1, schema);
            statement.setString(2, table);
            final ResultSet resultSet = statement.executeQuery(LIST_TABLES_QUERY);
            while (resultSet.next()) {
                columns.add(resultSet.getString(TABLES_COLUMN_NAME));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return columns;
    }

    public static PostgreSQLConnection create(
            String hostName, Integer port, String databaseName, String userName, String password) {
        final String connectionUrl = POSTGRESQL_URL_PREFIX + hostName + ":" + port + "/" + databaseName;
        return new PostgreSQLConnection(connectionUrl, userName, password);
    }
}

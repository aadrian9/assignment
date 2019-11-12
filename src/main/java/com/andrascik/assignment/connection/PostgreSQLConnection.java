package com.andrascik.assignment.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostgreSQLConnection {
    private static final String POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";
    private static final String GET_TABLE_DATA_QUERY = "SELECT * FROM '?'.'?' LIMIT 10";

    private final String connectionUrl;
    private final String userName;
    private final String password;

    private PostgreSQLConnection(String connectionUrl, String userName, String password) {
        this.connectionUrl = connectionUrl;
        this.userName = userName;
        this.password = password;
    }

    //TODO split all this into at least two classes

    /**
     * Lists all schemas for this database connection.
     *
     * @return
     */
    public List<String> listSchemas() {
        final List<String> schemas = new ArrayList<>();
        try (final var connection = DriverManager.getConnection(connectionUrl, userName, password)) {
            final var resultSet = connection.getMetaData().getSchemas();
            while (resultSet.next()){
                schemas.add(resultSet.getString("TABLE_SCHEM"));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return schemas;
    }

    /**
     * Lists all tables in specified schema for this database connection.
     *
     * @param schema
     * @return
     */
    public List<String> listTables(String schema) {
        final var tables = new ArrayList<String>();
        try (final var connection = DriverManager.getConnection(connectionUrl, userName, password)) {
            final var resultSet = connection
                    .getMetaData()
                    .getTables(null, schema, "%", null);
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return tables;
    }

    /**
     * Lists all tables in specified schema and table for this database connection.
     *
     * @param schema
     * @param table
     * @return
     */
    public List<ColumnInfo> listColumns(String schema, String table) {
        final var columns = new ArrayList<ColumnInfo>();
        try (final var connection = DriverManager.getConnection(connectionUrl, userName, password)) {
            final var columnMetaData = connection
                    .getMetaData()
                    .getColumns(null, schema, table, "%");
            final var primaryKeys = parsePrimaryKeys(
                    connection.getMetaData()
                            .getPrimaryKeys(null, schema, table));
            final var foreignKeys = parseForeignKeys(
                    connection.getMetaData().getImportedKeys(null, schema, table));
            while (columnMetaData.next()) {
                //TODO primary and foreign keys
                columns.add(new ColumnInfo(
                        columnMetaData.getString("COLUMN_NAME"),
                        columnMetaData.getString("TYPE_NAME"),
                        false,
                        false
                ));
            }
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
        return columns;
    }

    private List<String> parsePrimaryKeys(ResultSet resultSet) {
        //TODO parse primary keys
        return Collections.emptyList();
    }

    private List<String> parseForeignKeys(ResultSet resultSet) {
        //TODO parse foreign keys
        return Collections.emptyList();
    }

    /**
     * Reads a data preview of a table.
     *
     * @param schema
     * @param table
     * @return
     */
    public TablePreview previewData(String schema, String table) {
        try (final var connection = DriverManager.getConnection(connectionUrl, userName, password);
             final var statement = connection.prepareStatement(GET_TABLE_DATA_QUERY)) {
            statement.setString(1, schema);
            statement.setString(2, table);
            return parsePreviewResult(statement.executeQuery());
        } catch (SQLException e) {
            throw new PostgreSQLConnectionException(e);
        }
    }

    private TablePreview parsePreviewResult(ResultSet resultSet) throws SQLException {
        final var columnNames = parseColumnNames(resultSet.getMetaData());
        final var tableData = new TablePreview(columnNames);
        while (resultSet.next()) {
            tableData.addRow(parseRow(resultSet, columnNames));
        }
        return tableData;
    }

    private RowData parseRow(ResultSet resultSet, List<String> columns) throws SQLException {
        final var data = new ArrayList<String>();
        for (final var column : columns) {
            data.add(resultSet.getString(column));
        }
        return new RowData(data);
    }

    private List<String> parseColumnNames(ResultSetMetaData metaData) throws SQLException {
        final var names = new ArrayList<String>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            names.add(metaData.getColumnName(i));
        }
        return names;
    }

    /**
     * Factory for PostgreSQLConnection.
     *
     * @param hostName
     * @param port
     * @param databaseName
     * @param userName
     * @param password
     * @return
     */
    public static PostgreSQLConnection create(
            String hostName, Integer port, String databaseName, String userName, String password) {
        final String connectionUrl = POSTGRESQL_URL_PREFIX + hostName + ":" + port + "/" + databaseName;
        return new PostgreSQLConnection(connectionUrl, userName, password);
    }
}

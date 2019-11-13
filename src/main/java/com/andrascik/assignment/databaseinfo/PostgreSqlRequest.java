package com.andrascik.assignment.databaseinfo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlRequest {
    private static final String GET_TABLE_DATA_QUERY = "SELECT * FROM '?'.'?' LIMIT 10";
    private static final String GET_ROW_COUNT_QUERY = "SELECT COUNT(*) FROM '?'.'?'";

    private final PostgreSqlConnection connection;

    public PostgreSqlRequest(PostgreSqlConnection connection) {
        this.connection = connection;
    }

    public List<String> listSchemas() {
        final List<String> schemas = new ArrayList<>();
        try (final var resultSet = connection.getMetaData().getSchemas()) {
            while (resultSet.next()) {
                schemas.add(resultSet.getString("TABLE_SCHEM"));
            }
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
        return schemas;
    }

    public List<TableInfo> listTables(String schema) {
        final var tables = new ArrayList<TableInfo>();
        try (final var resultSet = connection.getMetaData().getTables(null, schema, "%", null)) {
            while (resultSet.next()) {
                final var current = new TableInfo(
                        resultSet.getString("TABLE_NAME"),
                        resultSet.getString("TABLE_TYPE"));
                tables.add(current);
            }
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
        return tables;
    }

    public List<ColumnInfo> listColumns(String schema, String table) {
        final var metaData = connection.getMetaData();
        try (final var columnMetaData = metaData.getColumns(null, schema, table, "%");
             final var primaryKeyMetaData = metaData.getPrimaryKeys(null, schema, table);
             final var foreignKeyMetaData = metaData.getImportedKeys(null, schema, table)) {
            final var primaryKeys = parsePrimaryKeys(primaryKeyMetaData);
            final var foreignKeys = parseForeignKeys(foreignKeyMetaData);
            return parseColumns(columnMetaData, primaryKeys, foreignKeys);
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    public TablePreview previewData(String schema, String table) {
        try (final var statement = connection.prepareStatement(GET_TABLE_DATA_QUERY)) {
            statement.setString(1, schema);
            statement.setString(2, table);
            return parsePreviewResult(statement.executeQuery());
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    public TableStatistics getTableStatistics(String schema, String table) {
        try (final var statement = connection.prepareStatement(GET_ROW_COUNT_QUERY);
             final var columnMetaData = connection.getMetaData().getColumns(null, schema, table, "%")) {
            statement.setString(1, schema);
            statement.setString(2, table);
            return new TableStatistics(parseRowCount(statement.executeQuery()), parseColumnCount(columnMetaData));
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    //==================================================================================================================

    private List<ColumnInfo> parseColumns(
            ResultSet columns, List<String> primaryKeys, List<String> foreignKeys) throws SQLException {
        final var result = new ArrayList<ColumnInfo>();
        while (columns.next()) {
            final var columnName = columns.getString("COLUMN_NAME");
            result.add(new ColumnInfo(
                    columnName,
                    columns.getString("TYPE_NAME"),
                    primaryKeys.contains(columnName),
                    foreignKeys.contains(columnName)
            ));
        }
        return result;
    }

    private List<String> parsePrimaryKeys(ResultSet resultSet) throws SQLException {
        final var primaryKeys = new ArrayList<String>();
        while (resultSet.next()) {
            primaryKeys.add(resultSet.getString("COLUMN_NAME"));
        }
        return primaryKeys;
    }

    private List<String> parseForeignKeys(ResultSet resultSet) throws SQLException {
        final var foreignKeys = new ArrayList<String>();
        while (resultSet.next()) {
            foreignKeys.add(resultSet.getString("FKCOLUMN_NAME"));
        }
        return foreignKeys;
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

    private int parseColumnCount(ResultSet columnMetaData) throws SQLException {
        int count = 0;
        while (columnMetaData.next()){
            count++;
        }
        return count;
    }

    private int parseRowCount(ResultSet queryResult) throws SQLException {
        if (!queryResult.next()) {
            throw new PostgreSqlConnectionException("No result found for row count query.");
        }
        return queryResult.getInt(1);
    }
}

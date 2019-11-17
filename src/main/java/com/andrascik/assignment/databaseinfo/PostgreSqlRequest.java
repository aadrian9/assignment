package com.andrascik.assignment.databaseinfo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sends requests to a PostgreSQL database.
 */
class PostgreSqlRequest {
    private static final String ROW_COUNT_LABEL = "ROW_COUNT";
    private static final String MIN_VALUE_LABEL = "MIN_VALUE";
    private static final String MAX_VALUE_LABEL = "MAX_VALUE";
    private static final String AVG_VALUE_LABEL = "AVG_VALUE";
    private static final String MEDIAN_VALUE_LABEL = "MEDIAN_VALUE";
    private static final String GET_TABLE_DATA_QUERY = "SELECT * FROM '?'.'?' LIMIT 10";
    private static final String GET_ROW_COUNT_QUERY = "SELECT count(*) AS " + ROW_COUNT_LABEL + " FROM '?'.'?'";
    private static final String GET_COLUMN_MIN_VALUE_QUERY = "SELECT min(?) AS " + MIN_VALUE_LABEL + " FROM '?'.'?'";
    private static final String GET_COLUMN_MAX_VALUE_QUERY = "SELECT max(?) AS " + MAX_VALUE_LABEL + " FROM '?'.'?'";
    private static final String GET_COLUMN_AVG_VALUE_QUERY = "SELECT avg(?) AS " + AVG_VALUE_LABEL + " FROM '?'.'?'";
    private static final String GET_COLUMN_MEDIAN_VALUE_QUERY =
            "SELECT percentile_disc(0.5) WITHIN GROUP (ORDER BY '?'.'?') AS " + MEDIAN_VALUE_LABEL + " FROM '?'.'?'";

    private final PostgreSqlConnection connection;

    PostgreSqlRequest(PostgreSqlConnection connection) {
        this.connection = connection;
    }

    List<String> listSchemas() {
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

    List<TableInfo> listTables(String schema) {
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

    List<ColumnInfo> listColumns(String schema, String table) {
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

    TableData previewData(String schema, String table) {
        try (final var statement = connection.prepareStatement(GET_TABLE_DATA_QUERY)) {
            statement.setString(1, schema);
            statement.setString(2, table);
            return parsePreviewResult(statement.executeQuery());
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    TableStatistics getTableStatistics(String schema, String table) {
        try (final var statement = connection.prepareStatement(GET_ROW_COUNT_QUERY);
             final var columnMetaData = connection.getMetaData().getColumns(null, schema, table, "%")) {
            statement.setString(1, schema);
            statement.setString(2, table);
            return new TableStatistics(parseRowCount(statement.executeQuery()), parseColumnCount(columnMetaData));
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    ColumnStatistics getColumnStatistics(String schema, String table, String column) {
        return new ColumnStatistics(
                getColumnMinValue(schema, table, column),
                getColumnMaxValue(schema, table, column),
                getColumnAverageValue(schema, table, column),
                getColumnMedianValue(schema, table, column)
        );
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

    private TableData parsePreviewResult(ResultSet resultSet) throws SQLException {
        final var columnNames = parseColumnNames(resultSet.getMetaData());
        final var tableData = new TableData(columnNames);
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
        while (columnMetaData.next()) {
            count++;
        }
        return count;
    }

    private int parseRowCount(ResultSet queryResult) throws SQLException {
        if (!queryResult.next()) {
            throw new PostgreSqlConnectionException("No result found for row count query.");
        }
        return queryResult.getInt(ROW_COUNT_LABEL);
    }

    private String getColumnMinValue(String schema, String table, String column) {
        return processSimpleStatisticsQuery(schema, table, column, GET_COLUMN_MIN_VALUE_QUERY, MIN_VALUE_LABEL);
    }

    private String getColumnMaxValue(String schema, String table, String column) {
        return processSimpleStatisticsQuery(schema, table, column, GET_COLUMN_MAX_VALUE_QUERY, MAX_VALUE_LABEL);
    }

    private String getColumnAverageValue(String schema, String table, String column) {
        return processSimpleStatisticsQuery(schema, table, column, GET_COLUMN_AVG_VALUE_QUERY, AVG_VALUE_LABEL);
    }

    private String processSimpleStatisticsQuery(
            String schema, String table, String column, String getColumnMinValueQuery, String minValueLabel) {
        try (final var statement = connection.prepareStatement(getColumnMinValueQuery)) {
            statement.setString(1, column);
            statement.setString(2, schema);
            statement.setString(3, table);
            final var queryResult = statement.executeQuery();
            if (!queryResult.next()) {
                throw new PostgreSqlConnectionException("No result found for " + minValueLabel + " value query.");
            }
            return queryResult.getString(minValueLabel);
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }

    private String getColumnMedianValue(String schema, String table, String column) {
        try (final var statement = connection.prepareStatement(GET_COLUMN_MEDIAN_VALUE_QUERY)) {
            statement.setString(1, table);
            statement.setString(2, column);
            statement.setString(3, schema);
            statement.setString(4, table);
            final var queryResult = statement.executeQuery();
            if (!queryResult.next()) {
                throw new PostgreSqlConnectionException("No result found for " + MEDIAN_VALUE_LABEL + " value query.");
            }
            return queryResult.getString(MEDIAN_VALUE_LABEL);
        } catch (SQLException e) {
            throw new PostgreSqlConnectionException(e);
        }
    }
}

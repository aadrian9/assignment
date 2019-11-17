package com.andrascik.assignment.databaseinfo;

import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class DatabaseInfoService {
    private final PostgreSqlConnectionFactory connectionFactory;
    private final ConnectionPersistenceService connectionPersistence;

    @Autowired
    public DatabaseInfoService(
            PostgreSqlConnectionFactory connectionFactory,
            ConnectionPersistenceService connectionPersistence) {
        this.connectionFactory = connectionFactory;
        this.connectionPersistence = connectionPersistence;
    }

    /**
     * Lists names of all database schemas.
     * @return
     */
    public Optional<List<String>> listSchemas(long connectionId) {
        return handleDatabaseAction(
                connectionId,
                PostgreSqlRequest::listSchemas
        );
    }

    /**
     * Lists information about all tables within a schema.
     * @param schema
     * @return
     */
    public Optional<List<TableInfo>> listTables(long connectionId, String schema) {
        return handleDatabaseAction(
                connectionId,
                request -> request.listTables(schema)
        );
    }

    /**
     * Lists information about all columns within a table.
     * @param schema
     * @param table
     * @return
     */
    public Optional<List<ColumnInfo>> listColumns(long connectionId, String schema, String table) {
        return handleDatabaseAction(
                connectionId,
                request -> request.listColumns(schema, table)
        );
    }

    /**
     * Returns a preview of data for a table with sample rows containing all columns.
     * @param schema
     * @param table
     * @return
     */
    public Optional<TableData> previewData(long connectionId, String schema, String table) {
        return handleDatabaseAction(
                connectionId,
                request -> request.previewData(schema, table)
        );
    }

    /**
     * Determine number of rows and columns in a table.
     * @param schema
     * @param table
     * @return
     */
    public Optional<TableStatistics> getTableStatistics(long connectionId, String schema, String table) {
        return handleDatabaseAction(
                connectionId,
                request -> request.getTableStatistics(schema, table)
        );
    }

    /**
     * Determine basic statistics for a single column in a table.
     * @param schema
     * @param table
     * @param column
     * @return
     */
    public Optional<ColumnStatistics> getColumnStatistics(
            long connectionId, String schema, String table, String column) {
        return handleDatabaseAction(
                connectionId,
                request -> request.getColumnStatistics(schema, table, column)
        );
    }

    //=================================================================================================================

    private <T> Optional<T> handleDatabaseAction(long connectionId, Function<PostgreSqlRequest, T> action) {
        final var connectionInfo = connectionPersistence.findConnection(connectionId);
        if (connectionInfo.isEmpty()) {
            return Optional.empty();
        }
        try (final var connection = createConnection(connectionInfo.get())) {
            final var request = new PostgreSqlRequest(connection);
            return Optional.of(action.apply(request));
        }
    }

    private PostgreSqlConnection createConnection(ConnectionData connectionInfo) {
        return connectionFactory.create(
                connectionInfo.getHostname(),
                connectionInfo.getPort(),
                connectionInfo.getDatabaseName(),
                connectionInfo.getUserName(),
                connectionInfo.getPassword()
        );
    }
}

package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.*;
import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

/**
 * Controller querying databases for their contents.
 */
//TODO swagger
@RestController
@RequestMapping("/api/connections")
public class DatabaseInfoController {
    private final ConnectionPersistenceService persistenceService;
    private final PostgreSqlConnectionFactory factory;

    @Autowired
    public DatabaseInfoController(
            ConnectionPersistenceService persistenceService,
            PostgreSqlConnectionFactory factory) {
        this.persistenceService = persistenceService;
        this.factory = factory;
    }

    @GetMapping("/{connectionId}/schemas")
    public ResponseEntity<List<String>> listSchemas(
            @PathVariable long connectionId) {
        return handleDatabaseAction(connectionId, PostgreSqlRequest::listSchemas);
    }

    @GetMapping("/{connectionId}/schemas/{schema}")
    public ResponseEntity<List<TableInfoDto>> listTables(
            @PathVariable long connectionId,
            @PathVariable String schema) {
        return handleDatabaseAction(connectionId, request -> TableInfoTranslator.translate(request.listTables(schema)));
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}")
    public ResponseEntity<List<ColumnInfoDto>> listColumns(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> ColumnInfoTranslator.translate(request.listColumns(schema, table))
        );
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/data")
    public ResponseEntity<TablePreviewDto> previewData(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> TablePreviewTranslator.translate(request.previewData(schema, table))
        );
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/statistics")
    public ResponseEntity<TableStatisticsDto> tableStatistics(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> StatisticsTranslator.translate(table, request.getTableStatistics(schema, table))
        );
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/{column}/statistics")
    public ResponseEntity<ColumnStatisticsDto> columnStatistics(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table,
            @PathVariable String column) {
        return handleDatabaseAction(
                connectionId,
                request -> StatisticsTranslator.translate(
                        table,
                        column,
                        request.getColumnStatistics(schema, table, column)
                )
        );
    }


    //=================================================================================================================

    private <T> ResponseEntity<T> handleDatabaseAction(long connectionId, Function<PostgreSqlRequest, T> action) {
        final var connectionInfo = persistenceService.findConnection(connectionId);
        if (connectionInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try (final var connection = createConnection(connectionInfo.get())) {
            final var request = new PostgreSqlRequest(connection);
            return ResponseEntity.ok(action.apply(request));
        }
    }

    private PostgreSqlConnection createConnection(ConnectionData connectionInfo) {
        return factory.create(
                connectionInfo.getHostname(),
                connectionInfo.getPort(),
                connectionInfo.getDatabaseName(),
                connectionInfo.getUserName(),
                connectionInfo.getPassword()
        );
    }
}

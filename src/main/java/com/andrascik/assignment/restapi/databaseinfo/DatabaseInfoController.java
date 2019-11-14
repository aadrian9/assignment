package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.*;
import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import io.swagger.annotations.*;
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
@Api(value = "Database information")
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

    @ApiOperation(value = "List schemas for a database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of schema names"),
            @ApiResponse(code = 404, message = "Connection not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas")
    public ResponseEntity<List<String>> listSchemas(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId) {
        return handleDatabaseAction(connectionId, PostgreSqlRequest::listSchemas);
    }

    @ApiOperation(value = "List tables in a database schema")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list table information"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas/{schema}")
    public ResponseEntity<List<TableInfoDto>> listTables(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @ApiParam(value = "Name of the database schema", required = true) @PathVariable String schema) {
        return handleDatabaseAction(connectionId, request -> TableInfoTranslator.translate(request.listTables(schema)));
    }

    @ApiOperation(value = "List columns in a database table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list column information"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}")
    public ResponseEntity<List<ColumnInfoDto>> listColumns(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @ApiParam(value = "Name of the database schema", required = true) @PathVariable String schema,
            @ApiParam(value = "Name of the database table", required = true) @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> ColumnInfoTranslator.translate(request.listColumns(schema, table))
        );
    }

    @ApiOperation(value = "Preview data of a database table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved table preview"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/data")
    public ResponseEntity<TablePreviewDto> previewData(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @ApiParam(value = "Name of the database schema", required = true) @PathVariable String schema,
            @ApiParam(value = "Name of the database table", required = true) @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> TablePreviewTranslator.translate(request.previewData(schema, table))
        );
    }

    @ApiOperation(value = "Get statistics about a table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved table statistics"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/statistics")
    public ResponseEntity<TableStatisticsDto> tableStatistics(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @ApiParam(value = "Name of the database schema", required = true) @PathVariable String schema,
            @ApiParam(value = "Name of the database table", required = true) @PathVariable String table) {
        return handleDatabaseAction(
                connectionId,
                request -> StatisticsTranslator.translate(table, request.getTableStatistics(schema, table))
        );
    }

    @ApiOperation(value = "Get statistics about a table column")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved column statistics"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 500, message = "Error during remote connection")
    })
    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/columns/{column}/statistics")
    public ResponseEntity<ColumnStatisticsDto> columnStatistics(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @ApiParam(value = "Name of the database schema", required = true) @PathVariable String schema,
            @ApiParam(value = "Name of the database table", required = true) @PathVariable String table,
            @ApiParam(value = "Name of the table column", required = true) @PathVariable String column) {
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

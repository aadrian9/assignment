package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.DatabaseInfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller querying databases for their contents.
 */
@Api(value = "Database information")
@RestController
@RequestMapping("/api/connections")
public class DatabaseInfoController {
    private final DatabaseInfoService infoService;

    @Autowired
    public DatabaseInfoController(DatabaseInfoService infoService) {
        this.infoService = infoService;
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
        return ResponseEntity.of(
                infoService.listSchemas(connectionId)
        );
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
        final var tables = infoService.listTables(connectionId, schema);
        if (tables.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                tables.get()
                        .stream()
                        .map(DatabaseInfoTranslator::translate)
                        .collect(Collectors.toList())
        );
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
        final var columns = infoService.listColumns(connectionId, schema, table);
        if (columns.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                columns.get()
                        .stream()
                        .map(DatabaseInfoTranslator::translate)
                        .collect(Collectors.toList())
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
        final var tablePreview = infoService.previewData(connectionId, schema, table);
        if (tablePreview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                DatabaseInfoTranslator.translate(tablePreview.get())
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
        final var tableStatistics = infoService.getTableStatistics(connectionId, schema, table);
        if (tableStatistics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                DatabaseInfoTranslator.translate(table, tableStatistics.get())
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
        final var columnStatistics = infoService.getColumnStatistics(connectionId, schema, table, column);
        if (columnStatistics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                DatabaseInfoTranslator.translate(
                        table,
                        column,
                        columnStatistics.get()
                )
        );
    }
}

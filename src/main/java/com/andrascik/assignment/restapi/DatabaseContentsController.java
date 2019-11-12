package com.andrascik.assignment.restapi;

import com.andrascik.assignment.connection.ColumnInfo;
import com.andrascik.assignment.connection.PostgreSQLConnection;
import com.andrascik.assignment.connection.RowData;
import com.andrascik.assignment.connection.TablePreview;
import com.andrascik.assignment.persistence.ConnectionPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controller querying databases for their contents.
 */
@RestController
@RequestMapping("/api/connections")
public class DatabaseContentsController {
    private final ConnectionPersistenceService persistenceService;

    @Autowired
    public DatabaseContentsController(ConnectionPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @GetMapping("/{connectionId}/schemas")
    public ResponseEntity<List<String>> listSchemas(
            @PathVariable long connectionId) {
        return handleAction(connectionId, PostgreSQLConnection::listSchemas);
    }

    @GetMapping("/{connectionId}/schemas/{schema}")
    public ResponseEntity<List<String>> listTables(
            @PathVariable long connectionId,
            @PathVariable String schema) {
        return handleAction(connectionId, connection -> connection.listTables(schema));
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}")
    public ResponseEntity<List<ColumnInfoDto>> listColumns(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table) {
        //TODO beautify
        return handleAction(connectionId,
                connection -> connection.listColumns(schema, table)
                        .stream()
                        .map(this::translateColumnInfo)
                        .collect(Collectors.toList()));
    }

    //TODO TablePreviewDto is not necessary, List<RowDataDto> should suffice
    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}/data")
    public ResponseEntity<TablePreviewDto> previewData(
            @PathVariable long connectionId,
            @PathVariable String schema,
            @PathVariable String table) {
        //TODO wtf
        return handleAction(connectionId,
                connection -> translateTablePreview(connection.previewData(schema, table)));
    }

    //=================================================================================================================

    private <T> ResponseEntity<T> handleAction(long connectionId, Function<PostgreSQLConnection, T> databaseAction) {
        final var connection = createConnection(connectionId);
        if (connection.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(databaseAction.apply(connection.get()));
    }

    private Optional<PostgreSQLConnection> createConnection(long connectionId) {
        final var storedConnection = persistenceService.findConnection(connectionId);
        if (storedConnection.isEmpty()) {
            return Optional.empty();
        }
        final var connectionData = storedConnection.get();
        final var connection = PostgreSQLConnection.create(
                connectionData.getHostname(),
                connectionData.getPort(),
                connectionData.getDatabaseName(),
                connectionData.getUserName(),
                connectionData.getPassword());
        return Optional.of(connection);
    }

    private ColumnInfoDto translateColumnInfo(ColumnInfo columnInfo) {
        return new ColumnInfoDto(columnInfo.getName(), columnInfo.getDataType(), columnInfo.isPrimaryKey(), columnInfo.isForeignKey());
    }

    private TablePreviewDto translateTablePreview(TablePreview tablePreview) {
        return new TablePreviewDto(tablePreview.getColumnNames(),
                tablePreview.getRows()
                        .stream()
                        .map(this::translateRowData)
                        .collect(Collectors.toList()));
    }

    private RowDataDto translateRowData(RowData rowData) {
        //TODO this is not a good way
//        return new RowDataDto()
        return null;
    }
}


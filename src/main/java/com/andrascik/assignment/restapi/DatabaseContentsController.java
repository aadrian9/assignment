package com.andrascik.assignment.restapi;

import com.andrascik.assignment.connection.PostgreSQLConnection;
import com.andrascik.assignment.persistence.ConnectionData;
import com.andrascik.assignment.persistence.ConnectionPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controller querying databases for their contents.
 */
@RestController
@RequestMapping("/api/connections")
public class DatabaseContentsController {
    @Autowired
    private ConnectionPersistenceService persistenceService;

    @GetMapping("/{connectionId}/schemas")
    public ResponseEntity<List<String>> listSchemas(@PathVariable(required = true) long connectionId) {
        final var connection = createConnection(connectionId);
        if (connection.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(connection.get().listSchemas());
    }

    @GetMapping("/{connectionId}/schemas/{schema}")
    public ResponseEntity<List<String>> listTables(
            @PathVariable(required = true) long connectionId,
            @PathVariable(required = true) String schema) {
        final var connection = createConnection(connectionId);
        if (connection.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(connection.get().listTables(schema));
    }

    @GetMapping("/{connectionId}/schemas/{schema}/tables/{table}")
    public ResponseEntity<List<String>> listColumns(
            @PathVariable(required = true) long connectionId,
            @PathVariable(required = true) String schema,
            @PathVariable(required = true) String table) {
        final var connection = createConnection(connectionId);
        if (connection.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(connection.get().listColumns(schema, table));
    }

    private Optional<PostgreSQLConnection> createConnection(long connectionId) {
        final Optional<ConnectionData> storedConnection = persistenceService.findConnection(connectionId);
        if (storedConnection.isEmpty()) {
            return Optional.empty();
        }
        final ConnectionData connectionData = storedConnection.get();
        final PostgreSQLConnection connection = PostgreSQLConnection.create(
                connectionData.getHostname(),
                connectionData.getPort(),
                connectionData.getDatabaseName(),
                connectionData.getUserName(),
                connectionData.getPassword());
        return Optional.of(connection);
    }
}


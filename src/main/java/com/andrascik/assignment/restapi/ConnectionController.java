package com.andrascik.assignment.restapi;

import com.andrascik.assignment.persistence.ConnectionData;
import com.andrascik.assignment.persistence.ConnectionPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for persistence of database connection information.
 */
@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionPersistenceService persistenceService;

    @Autowired
    public ConnectionController(ConnectionPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @GetMapping
    public ResponseEntity<List<ConnectionDataDto>> listConnections() {
        return ResponseEntity.ok(
                persistenceService.listConnections().stream()
                        .map(this::translate)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ConnectionDataDto> createConnection(@RequestBody @NotNull ConnectionDataDto dto) {
        return ResponseEntity.of(persistenceService.saveConnection(translate(dto)).map(this::translate));
    }

    @PutMapping("/{connectionId}")
    public ResponseEntity<ConnectionDataDto> updateConnection(
            @PathVariable long connectionId,
            @RequestBody @NotNull ConnectionDataDto dto) {
        final var storedConnection = persistenceService.findConnection(connectionId);
        if (storedConnection.isEmpty()) {
            return ResponseEntity.of(Optional.empty());
        }
        final var toUpdate = storedConnection.get();
        toUpdate.setName(dto.getName());
        toUpdate.setHostname(dto.getHostname());
        toUpdate.setPort(dto.getPort());
        toUpdate.setDatabaseName(dto.getDatabaseName());
        toUpdate.setUserName(dto.getUserName());
        toUpdate.setPassword(dto.getPassword());

        return ResponseEntity.of(persistenceService.saveConnection(toUpdate).map(this::translate));
    }

    @DeleteMapping("/{connectionId}")
    public ResponseEntity deleteConnection(
            @PathVariable long connectionId) {
        if (!persistenceService.connectionExists(connectionId)) {
            return ResponseEntity.notFound().build();
        }
        persistenceService.deleteConnection(connectionId);
        return ResponseEntity.ok().build();
    }

    //=================================================================================================================

    private ConnectionData translate(ConnectionDataDto dto) {
        return new ConnectionData(
                dto.getName(),
                dto.getHostname(),
                dto.getPort(),
                dto.getDatabaseName(),
                dto.getUserName(),
                dto.getPassword());
    }

    private ConnectionDataDto translate(ConnectionData connectionData) {
        final var result = new ConnectionDataDto();
        result.setId(connectionData.getId());
        result.setName(connectionData.getName());
        result.setHostname(connectionData.getHostname());
        result.setPort(connectionData.getPort());
        result.setDatabaseName(connectionData.getDatabaseName());
        result.setUserName(connectionData.getUserName());
        result.setPassword(obfuscatePassword(connectionData.getPassword()));
        return result;
    }

    private String obfuscatePassword(String password) {
        //TODO
        //still not great since it gives away length but not sure what do we want
        return "*".repeat(password.length());
    }
}

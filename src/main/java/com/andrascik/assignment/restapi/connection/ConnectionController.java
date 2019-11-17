package com.andrascik.assignment.restapi.connection;

import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for persistence of database connection information.
 */
@Api(value = "Connection management")
@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionPersistenceService persistenceService;

    @Autowired
    public ConnectionController(ConnectionPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @ApiOperation(value = "View a list of stored database connections")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved connections"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    @GetMapping
    public ResponseEntity<List<ConnectionDataDto>> listConnections() {
        return ResponseEntity.ok(
                persistenceService.listConnections()
                        .stream()
                        .map(this::translate)
                        .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "Store a new database connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Newly stored connection with its assigned ID"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    @PostMapping
    public ResponseEntity<ConnectionDataDto> createConnection(
            @RequestBody @NotNull @Valid ConnectionDataDto dto) {
        return ResponseEntity.of(
                persistenceService
                        .saveConnection(translate(dto))
                        .map(this::translate)
        );
    }

    @ApiOperation(value = "Update information about stored connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated information"),
            @ApiResponse(code = 404, message = "Connection not found"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    @PutMapping("/{connectionId}")
    public ResponseEntity<ConnectionDataDto> updateConnection(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId,
            @RequestBody @NotNull @Valid ConnectionDataDto dto) {
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

        return ResponseEntity.of(
                persistenceService.saveConnection(toUpdate)
                        .map(this::translate)
        );
    }

    @ApiOperation(value = "Delete stored connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Connection not found"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    @DeleteMapping("/{connectionId}")
    public ResponseEntity deleteConnection(
            @ApiParam(value = "Id assigned to the database connection", required = true) @PathVariable long connectionId) {
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
        if (password == null) {
            return null;
        }
        return "****";
    }
}

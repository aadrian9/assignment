package com.andrascik.assignment.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Used for storing and retrieving connections from the repository.
 */
@Component
public class ConnectionPersistenceService {
    private final ConnectionDataRepository repository;

    @Autowired
    public ConnectionPersistenceService(ConnectionDataRepository repository) {
        this.repository = repository;
    }

    public List<ConnectionData> listConnections() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<ConnectionData> saveConnection(ConnectionData connectionData) {
        final var savedConnectionData = repository.save(connectionData);
        return savedConnectionData == null ? Optional.empty() : Optional.of(savedConnectionData);
    }

    public boolean connectionExists(long id) {
        return repository.existsById(id);
    }

    public Optional<ConnectionData> findConnection(long id) {
        return repository.findById(id);
    }


    public void deleteConnection(long id) {
        repository.deleteById(id);
    }
}

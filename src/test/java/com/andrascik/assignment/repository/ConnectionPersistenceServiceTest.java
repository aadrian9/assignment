package com.andrascik.assignment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConnectionPersistenceServiceTest {
    private static final long CONNECTION_A_ID = 1L;
    private static final long CONNECTION_B_ID = 2L;

    private ConnectionPersistenceService persistenceService;
    private ConnectionDataRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(ConnectionDataRepository.class);
        persistenceService = new ConnectionPersistenceService(repository);
    }

    @Test
    void findConnection_exists_correctlyReturned() {
        doAnswer(i -> Optional.of(mockConnectionDao(i.getArgument(0))))
                .when(repository)
                .findById(anyLong());

        final var actual = persistenceService.findConnection(CONNECTION_A_ID);

        assertThat(actual.get())
                .extracting(ConnectionData::getId)
                .isEqualTo(CONNECTION_A_ID);
    }

    @Test
    void findConnection_notFound_resultEmpty() {
        doReturn(Optional.empty())
                .when(repository)
                .findById(anyLong());

        final var actual = persistenceService.findConnection(CONNECTION_A_ID);

        assertThat(actual)
                .isEmpty();
    }

    @Test
    void connectionsExists_true_correct() {
        doReturn(true)
                .when(repository)
                .existsById(anyLong());

        final var actual = persistenceService.connectionExists(CONNECTION_A_ID);

        assertThat(actual)
                .isTrue();
    }

    @Test
    void connectionsExists_false_correct() {
        doReturn(false)
                .when(repository)
                .existsById(anyLong());

        final var actual = persistenceService.connectionExists(CONNECTION_A_ID);

        assertThat(actual)
                .isFalse();
    }

    @Test
    void deleteConnection_deleteIsCalled() {
        persistenceService.deleteConnection(CONNECTION_A_ID);

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void createConnection_created_correctlyReturned() {
        doAnswer(i -> mockConnectionDao(CONNECTION_A_ID))
                .when(repository)
                .save(any(ConnectionDataDao.class));

        final var actual = persistenceService
                .saveConnection(new ConnectionData(new ConnectionDataDao()));

        assertThat(actual.get())
                .extracting(ConnectionData::getId)
                .isEqualTo(CONNECTION_A_ID);
    }

    @Test
    void createConnection_failed_resultEmpty() {
        doReturn(Optional.empty())
                .when(repository)
                .findById(anyLong());

        final var actual = persistenceService.saveConnection(mockConnection(CONNECTION_A_ID));

        assertThat(actual)
                .isEmpty();
    }

    @Test
    void listConnections_allConnectionsReturned() {
        doAnswer(i -> mockTwoConnectionsDao()).when(repository).findAll();

        final var actual = persistenceService.listConnections();

        assertThat(actual.size())
                .isEqualTo(2);
    }

    @Test
    void listConnections_noConnections_emptyList() {
        doReturn(new ArrayList<ConnectionDataDao>()).when(repository).findAll();

        final var actual = persistenceService.listConnections();

        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }

    //=================================================================================================================

    private ConnectionData mockConnection(long id) {
        return new ConnectionData(mockConnectionDao(id));
    }

    private Iterable<ConnectionDataDao> mockTwoConnectionsDao() {
        final var result = new ArrayList<ConnectionDataDao>();
        result.add(mockConnectionDao(CONNECTION_A_ID));
        result.add(mockConnectionDao(CONNECTION_B_ID));
        return result;
    }

    private ConnectionDataDao mockConnectionDao(long id) {
        final var result = new ConnectionDataDao();
        result.setId(id);
        return result;
    }

}
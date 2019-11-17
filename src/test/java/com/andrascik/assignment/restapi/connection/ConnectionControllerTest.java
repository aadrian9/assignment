package com.andrascik.assignment.restapi.connection;

import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConnectionControllerTest {
    private static final Long CONNECTION_A_ID = 1L;
    private static final Long CONNECTION_B_ID = 2L;
    private static final String CONNECTION_NAME = "connection";
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final String DATABASE_NAME = "myDb";
    private static final String USER_NAME = "user";
    private static final String USER_PASSWORD = "pass123";

    private ConnectionController controller;
    private ConnectionPersistenceService persistenceService;

    @BeforeEach
    void setUp() {
        persistenceService = mock(ConnectionPersistenceService.class);
        controller = new ConnectionController(persistenceService);
    }

    @Test
    void listConnections_allReturned() {
        doReturn(mockTwoConnections()).when(persistenceService).listConnections();

        final var actual = controller.listConnections().getBody();

        assertThat(actual)
                .extracting(ConnectionDataDto::getId)
                .containsExactlyInAnyOrder(CONNECTION_A_ID, CONNECTION_B_ID);
    }

    @Test
    void createConnection_serviceCalled() {
        controller.createConnection(new ConnectionDataDto());

        verify(persistenceService, times(1)).saveConnection(any(ConnectionData.class));
    }

    @Test
    void updateConnection_exists_updated() {
        doAnswer(i -> Optional.of(mockConnection(i.getArgument(0)))).when(persistenceService).findConnection(anyLong());
        doAnswer(i -> Optional.of(i.getArgument(0))).when(persistenceService).saveConnection(any(ConnectionData.class));

        final var connectionDto = new ConnectionDataDto();
        connectionDto.setName(CONNECTION_NAME);
        final var actual = controller.updateConnection(CONNECTION_A_ID, connectionDto).getBody();

        assertThat(actual)
                .extracting(ConnectionDataDto::getId)
                .isEqualTo(CONNECTION_A_ID);
        assertThat(actual)
                .extracting(ConnectionDataDto::getName)
                .isEqualTo(CONNECTION_NAME);
    }

    @Test
    void updateConnection_doesntExists_notFound() {
        doAnswer(i -> Optional.empty()).when(persistenceService).findConnection(anyLong());
        doAnswer(i -> Optional.empty()).when(persistenceService).saveConnection(any(ConnectionData.class));

        final var connectionDto = new ConnectionDataDto();
        connectionDto.setName(CONNECTION_NAME);
        final var actual = controller.updateConnection(CONNECTION_A_ID, connectionDto);
        assertThat(actual)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual)
                .extracting(ResponseEntity::getBody)
                .isNull();
    }

    @Test
    void deleteConnection_exists_updated() {
        doReturn(true).when(persistenceService).connectionExists(anyLong());

        final var actual = controller.deleteConnection(CONNECTION_A_ID);

        assertThat(actual)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteConnection_doesntExists_notFound() {
        doReturn(false).when(persistenceService).connectionExists(anyLong());

        final var actual = controller.deleteConnection(CONNECTION_A_ID);

        assertThat(actual)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    //=================================================================================================================

    private List<ConnectionData> mockTwoConnections() {
        final var result = new ArrayList<ConnectionData>();
        result.add(mockConnection(CONNECTION_A_ID));
        result.add(mockConnection(CONNECTION_B_ID));
        return result;
    }

    private ConnectionData mockConnection(long id) {
        final var result = new ConnectionData(CONNECTION_NAME, HOSTNAME, PORT, DATABASE_NAME, USER_NAME, USER_PASSWORD);
        result.setId(id);
        return result;
    }

}
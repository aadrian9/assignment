package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.DatabaseInfoService;
import com.andrascik.assignment.databaseinfo.PostgreSqlConnection;
import com.andrascik.assignment.repository.ConnectionData;
import com.andrascik.assignment.repository.ConnectionPersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DatabaseInfoControllerIntegrationTest {
    private static final Long CONNECTION_ID = 1L;
    private static final String SCHEMA_NAME = "schema";
    private static final String SCHEMAS_COLUMN_LABEL = "TABLE_SCHEM";

    private MockMvc mockMvc;
    private DatabaseInfoController controller;
    private DatabaseInfoService databaseInfoService;
    private PostgreSqlConnection connectionMock;
    private ConnectionPersistenceService persistenceService;

    @BeforeEach
    void beforeEach() throws SQLException {
        persistenceService = mock(ConnectionPersistenceService.class);
        connectionMock = mock(PostgreSqlConnection.class);
        databaseInfoService = new DatabaseInfoService(new MockedConnectionFactory(connectionMock), persistenceService);
        controller = new DatabaseInfoController(databaseInfoService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        setupServices();
    }

    @Test
    void listSchemas_existingConnection_isSuccess() throws Exception {
        final var response = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/connections/" + CONNECTION_ID + "/schemas"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<String> responseObject = Arrays.asList(
                new ObjectMapper().readValue(response.getContentAsString(), String[].class)
        );

        assertThat(responseObject)
                .containsExactly(SCHEMA_NAME);
    }

    //=================================================================================================================

    private void setupServices() throws SQLException {
        doAnswer(this::mockFindConnection)
                .when(persistenceService).findConnection(anyLong());
        doReturn(mockMetaData())
                .when(connectionMock).getMetaData();
    }

    private DatabaseMetaData mockMetaData() throws SQLException {
        final var metadata = mock(DatabaseMetaData.class);
        doReturn(mockDbMetaDataResult())
                .when(metadata).getSchemas();
        return metadata;
    }

    private ResultSet mockDbMetaDataResult() throws SQLException {
        final var resultSet = mock(ResultSet.class);
        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        doAnswer(this::dbMetaDataGetString)
                .when(resultSet).getString(anyString());
        return resultSet;
    }

    private String dbMetaDataGetString(InvocationOnMock invocationOnMock) {
        if (SCHEMAS_COLUMN_LABEL.equals(invocationOnMock.getArgument(0))) {
            return SCHEMA_NAME;
        }
        return null;
    }

    private Optional<ConnectionData> mockFindConnection(InvocationOnMock invocationOnMock) {
        if (CONNECTION_ID.equals(invocationOnMock.getArgument(0))) {
            return Optional.of(
                    new ConnectionData(CONNECTION_ID, "", "", 0, "", "", "")
            );
        }
        return Optional.empty();
    }
}
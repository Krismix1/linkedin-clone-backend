package org.autopotato.linkedinclonebackend.unit.controllers;

import static org.autopotato.linkedinclonebackend.controllers.ConnectionController.NewConnectionRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.autopotato.linkedinclonebackend.controllers.ConnectionController;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ConnectionControllerTest {
    @InjectMocks
    ConnectionController connectionController;

    @Mock
    ConnectionRequestService connectionRequestService;

    NewConnectionRequestDTO newConnectionRequestDTO;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper jsonMapper;

    @BeforeEach
    final void setUp() {
        jsonMapper = new ObjectMapper();
    }

    @Test
    final void createConnectionRequest() {
        newConnectionRequestDTO = new NewConnectionRequestDTO(1L, 2L);

        when(connectionRequestService.create(anyLong(), anyLong()))
            .thenReturn(new ConnectionRequest(1, new Person(1), new Person(2)));

        var response = connectionController.createConnectionRequest(
            newConnectionRequestDTO
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("Create ConnectionRequest MethodArgumentNotValid")
    @ParameterizedTest(name = "Test with senderId: {0} and receiverId: {1}")
    @CsvSource(
        value = { "NIL, 2", "1, NIL", "0, 2", "1, 0", "-1, 2", "1, -2" },
        nullValues = "NIL"
    )
    final void createConnectionRequestMethodArgumentNotValid(
        Long senderId,
        Long receiverId
    )
        throws Exception {
        newConnectionRequestDTO = new NewConnectionRequestDTO(senderId, receiverId);

        mockMvc
            .perform(
                post("/connections/requests")
                    .content(jsonMapper.writeValueAsString(newConnectionRequestDTO))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    final void createConnectionRequestSameSenderReceiverID() {
        newConnectionRequestDTO = new NewConnectionRequestDTO(1L, 1L);

        var response = connectionController.createConnectionRequest(
            newConnectionRequestDTO
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    final void getAllConnectionRequest() {
        ConnectionRequest request = new ConnectionRequest(
            1,
            new Person(1),
            new Person(2)
        );
        Iterable<ConnectionRequest> requests = Collections.singletonList(request);
        when(connectionRequestService.getAll()).thenReturn(requests);
        assertEquals(requests, connectionController.getAllConnectionRequests());
    }

    @Test
    final void deleteConnectionRequest() {
        var response = connectionController.deleteConnectionRequest(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    final void deleteConnectionRequestNoSuchElement() throws Exception {
        mockMvc
            .perform(delete("/connections/requests/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    final void acceptConnectionRequest() {
        var response = connectionController.acceptConnectionRequest(1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    final void acceptConnectionRequestNoSuchElement() throws Exception {
        mockMvc
            .perform(post("/connections/requests/accept/1"))
            .andExpect(status().isNotFound());
    }
}

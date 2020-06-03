package org.autopotato.linkedinclonebackend.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.autopotato.linkedinclonebackend.controllers.ConnectionController;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    ConnectionController.NewConnectionRequestDTO newConnectionRequestDTO;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper jsonMapper;

    @BeforeEach
    final void setUp() {
        jsonMapper = new ObjectMapper();
    }

    @Test
    final void createConnectionRequest() {
        newConnectionRequestDTO =
            new ConnectionController.NewConnectionRequestDTO(1L, 2L);

        when(connectionRequestService.create(anyLong(), anyLong()))
            .thenReturn(new ConnectionRequest(1, new Person(1), new Person(2)));

        var response = connectionController.createConnectionRequest(
            newConnectionRequestDTO
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    final void createConnectionRequestMethodArgumentNotValidSenderNull()
        throws Exception {
        newConnectionRequestDTO =
            new ConnectionController.NewConnectionRequestDTO(null, 2L);

        mockMvc
            .perform(
                post("/connections/requests")
                    .content(jsonMapper.writeValueAsString(newConnectionRequestDTO))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    final void createConnectionRequestMethodArgumentNotValidReceiverNull()
        throws Exception {
        newConnectionRequestDTO =
            new ConnectionController.NewConnectionRequestDTO(1L, null);

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
        newConnectionRequestDTO =
            new ConnectionController.NewConnectionRequestDTO(1L, 1L);

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
        assertEquals(requests, connectionController.getAllConnectionRequest());
    }

    @Test
    final void deleteConnectionRequest() throws ResourceNotFoundException {
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
    final void acceptConnectionRequest() throws ResourceNotFoundException {
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

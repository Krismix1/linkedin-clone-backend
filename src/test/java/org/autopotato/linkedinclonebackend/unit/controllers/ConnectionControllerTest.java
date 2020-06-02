package org.autopotato.linkedinclonebackend.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.autopotato.linkedinclonebackend.controllers.ConnectionController;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class ConnectionControllerTest {
    @InjectMocks
    ConnectionController connectionController;

    @Mock
    ConnectionRequestService connectionRequestService;

    ConnectionController.NewConnectionRequestDTO newConnectionRequestDTO;

    @BeforeEach
    final void setUp() {
        MockitoAnnotations.initMocks(this);
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
    final void createConnectionRequestMethodArgumentNotValid() {
        //        newConnectionRequestDTO =
        //            new ConnectionController.NewConnectionRequestDTO(null, 2L);
        //
        //        var response = connectionController.createConnectionRequest(
        //            newConnectionRequestDTO
        //        );
        //
        //        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        //        newConnectionRequestDTO =
        //            new ConnectionController.NewConnectionRequestDTO(1L, null);
        //
        //        response = connectionController.createConnectionRequest(newConnectionRequestDTO);
        //
        //        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // TODO: switch to HTTP requests
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
    final void deleteConnectionRequest() {
        var response = connectionController.deleteConnectionRequest(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    final void deleteConnectionRequestNoSuchElement() throws ResourceNotFoundException {
        doThrow(new ResourceNotFoundException(1))
            .when(connectionRequestService)
            .delete(anyLong());

        var response = connectionController.deleteConnectionRequest(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    final void acceptConnectionRequest() {
        var response = connectionController.acceptConnectionRequest(1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    final void acceptConnectionRequestNoSuchElement() throws ResourceNotFoundException {
        doThrow(new ResourceNotFoundException(1))
            .when(connectionRequestService)
            .accept(anyLong());

        var response = connectionController.acceptConnectionRequest(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

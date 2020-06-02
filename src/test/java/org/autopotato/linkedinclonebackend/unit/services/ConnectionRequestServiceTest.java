package org.autopotato.linkedinclonebackend.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRequestRepository;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.autopotato.linkedinclonebackend.services.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ConnectionRequestServiceTest {
    @InjectMocks
    ConnectionRequestService connectionRequestService;

    @Mock
    ConnectionService connectionService;

    @Mock
    MockConnectionRequestRepository mockConnectionRequestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void create() {
        ConnectionRequest request = new ConnectionRequest(
            1,
            new Person(1),
            new Person(2)
        );
        when(mockConnectionRequestRepository.save(any())).thenReturn(request);
        assertEquals(request, connectionRequestService.create(1, 2));
    }

    @Test
    final void getAll() {
        ConnectionRequest request = new ConnectionRequest(
            1,
            new Person(1),
            new Person(2)
        );
        Iterable<ConnectionRequest> requests = Collections.singletonList(request);
        when(mockConnectionRequestRepository.findAll()).thenReturn(requests);

        assertEquals(requests, connectionRequestService.getAll());
    }

    @Test
    final void deleteThrowingResourceNotFound() {
        doThrow(new NoSuchElementException())
            .when(mockConnectionRequestRepository)
            .deleteById(anyLong());
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
                connectionRequestService.delete(1);
            }
        );
    }

    @Test
    final void delete() throws ResourceNotFoundException {
        connectionRequestService.delete(1);
        verify(mockConnectionRequestRepository, times(1)).deleteById(anyLong());
    }

    @Test
    final void acceptThrowingResourceNotFound() {
        when(mockConnectionRequestRepository.findById(anyLong()))
            .thenReturn(Optional.empty());
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
                connectionRequestService.accept(1);
            }
        );
    }

    @Test
    final void accept() throws ResourceNotFoundException {
        ConnectionRequest request = new ConnectionRequest(
            1,
            new Person(1),
            new Person(2)
        );
        when(mockConnectionRequestRepository.findById(anyLong()))
            .thenReturn(Optional.of(request));

        connectionRequestService.accept(1);

        verify(mockConnectionRequestRepository, times(1)).findById(anyLong());
        verify(connectionService, times(1)).create(anyLong(), anyLong());
        verify(mockConnectionRequestRepository, times(1)).delete(any());
    }
}

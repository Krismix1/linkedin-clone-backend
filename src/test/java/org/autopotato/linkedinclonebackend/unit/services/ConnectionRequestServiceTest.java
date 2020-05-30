package org.autopotato.linkedinclonebackend.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRequestRepository;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ConnectionRequestServiceTest {
    @InjectMocks
    ConnectionRequestService connectionRequestService;

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
    final void deleteThrowingNoSuchElement() {
        doThrow(new NoSuchElementException())
            .when(mockConnectionRequestRepository)
            .deleteById(anyLong());
        assertThrows(
            NoSuchElementException.class,
            () -> {
                connectionRequestService.delete(1);
            }
        );
    }

    @Test
    final void delete() {
        // TODO: how do I test if it was successful?
        // fail("Implement me!");
    }

    @Test
    final void acceptThrowingNoSuchElement() {
        when(mockConnectionRequestRepository.findById(anyLong()))
            .thenReturn(Optional.empty());
        assertThrows(
            NoSuchElementException.class,
            () -> {
                connectionRequestService.accept(1);
            }
        );
    }

    @Test
    final void accept() {
        ConnectionRequest request = new ConnectionRequest(
            1,
            new Person(1),
            new Person(2)
        );
        when(mockConnectionRequestRepository.findById(anyLong()))
            .thenReturn(Optional.of(request));
        // TODO: again no clue how to test it ...
        // fail("Implement me!");
    }
}

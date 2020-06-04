package org.autopotato.linkedinclonebackend.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.NoSuchElementException;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.Connection;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRepository;
import org.autopotato.linkedinclonebackend.services.ConnectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {
    @InjectMocks
    ConnectionService connectionService;

    @Mock
    MockConnectionRepository mockConnectionRepository;

    @Test
    final void create() {
        Connection connection = new Connection(1, new Person(1), new Person(2));
        when(mockConnectionRepository.save(any())).thenReturn(connection);
        assertEquals(connection, connectionService.create(1, 2));
    }

    @Test
    final void getAll() {
        Connection connection = new Connection(1, new Person(1), new Person(2));
        Iterable<Connection> connections = Collections.singletonList(connection);
        when(mockConnectionRepository.findAll()).thenReturn(connections);

        assertEquals(connections, connectionService.getAll());
    }

    @Test
    final void delete() {
        connectionService.delete(1);
        verify(mockConnectionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    final void deleteThrowingResourceNotFound() {
        doThrow(new NoSuchElementException())
            .when(mockConnectionRepository)
            .deleteById(anyLong());
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
                connectionService.delete(1);
            }
        );
    }
}

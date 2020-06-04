package org.autopotato.linkedinclonebackend.services;

import java.util.NoSuchElementException;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.Connection;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private final MockConnectionRepository repo;

    @Autowired
    public ConnectionService(MockConnectionRepository repo) {
        this.repo = repo;
    }

    /**
     * Create a new {@link Connection}
     * @param senderId ID of the {@link Person} sending {@link Connection}
     * @param receiverId ID of the {@link Person} receiving {@link Connection}
     * @return {@link Connection} object after being saved/updated in the database
     */
    public Connection create(long senderId, long receiverId) {
        Connection connection = new Connection(
            new Person(senderId),
            new Person(receiverId)
        );

        return repo.save(connection);
    }

    /**
     * Gets all {@link Connection} from the database
     * @return {@link Iterable} of {@link Connection} in the database
     */
    public Iterable<Connection> getAll() {
        return repo.findAll();
    }

    /**
     * Deletes {@link Connection} from the database
     * @param id ID of the {@link Connection} to be deleted from the database
     * @throws NoSuchElementException if ID doesn't exist
     * @throws IllegalArgumentException if ID is {@literal null}
     */
    public void delete(long id)
        throws ResourceNotFoundException, IllegalArgumentException {
        try {
            repo.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(id);
        }
    }
}

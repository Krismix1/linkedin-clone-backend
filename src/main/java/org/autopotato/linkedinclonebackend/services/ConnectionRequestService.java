package org.autopotato.linkedinclonebackend.services;

import java.util.NoSuchElementException;
import org.autopotato.linkedinclonebackend.controllers.ResourceNotFoundException;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionRequestService {
    private final MockConnectionRequestRepository repo;
    private final ConnectionService connectionService;

    /**
     * Service handling ConnectionRequest logic
     * @param repo injected {@link ConnectionRequest} repository
     * @param connectionService {@link ConnectionService} used to create new connections
     */
    @Autowired
    public ConnectionRequestService(
        MockConnectionRequestRepository repo,
        ConnectionService connectionService
    ) {
        this.repo = repo;
        this.connectionService = connectionService;
    }

    /**
     * Create a new {@link ConnectionRequest}
     * @param senderId ID of the {@link Person} sending {@link ConnectionRequest}
     * @param receiverId ID of the {@link Person} receiving {@link ConnectionRequest}
     * @return {@link ConnectionRequest} object after being saved/updated in the database
     */
    public ConnectionRequest create(long senderId, long receiverId) {
        ConnectionRequest request = new ConnectionRequest(
            new Person(senderId),
            new Person(receiverId)
        );

        return repo.save(request);
    }

    /**
     * Gets all {@link ConnectionRequest} from the database
     * @return {@link Iterable} of {@link ConnectionRequest} in the database
     */
    public Iterable<ConnectionRequest> getAll() {
        return repo.findAll();
    }

    /**
     * Deletes {@link ConnectionRequest} from the database
     * @param id ID of the {@link ConnectionRequest} to be deleted from the database
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

    /**
     * Accepts {@link ConnectionRequest} from another user
     * @param id ID of {@link ConnectionRequest} to be accepted
     * @throws NoSuchElementException if ID doesn't exist
     * @throws IllegalArgumentException if ID is {@literal null}
     */
    public void accept(long id)
        throws ResourceNotFoundException, IllegalArgumentException {
        var optRequest = repo.findById(id);
        if (optRequest.isPresent()) {
            ConnectionRequest request = optRequest.get();
            connectionService.create(
                request.getSender().getId(),
                request.getReceiver().getId()
            );
            repo.delete(request);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}

package org.autopotato.linkedinclonebackend.services;

import java.util.NoSuchElementException;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.model.Person;
import org.autopotato.linkedinclonebackend.repositories.MockConnectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionRequestService {
    private final MockConnectionRequestRepository repo;
    private final ConnectionService connectionService;

    @Autowired
    public ConnectionRequestService(
        MockConnectionRequestRepository repo,
        ConnectionService connectionService
    ) {
        this.repo = repo;
        this.connectionService = connectionService;
    }

    public ConnectionRequest create(long senderId, long receiverId) {
        ConnectionRequest request = new ConnectionRequest(
            new Person(senderId),
            new Person(receiverId)
        );

        return repo.save(request);
    }

    public Iterable<ConnectionRequest> getAll() {
        return repo.findAll();
    }

    public void delete(long id) throws NoSuchElementException, IllegalArgumentException {
        repo.deleteById(id);
    }

    public void accept(long id) throws NoSuchElementException, IllegalArgumentException {
        var optRequest = repo.findById(id);
        if (optRequest.isPresent()) {
            ConnectionRequest request = optRequest.get();
            connectionService.create(
                request.getSender().getId(),
                request.getReceiver().getId()
            );
            repo.delete(request);
        } else {
            throw new NoSuchElementException();
        }
    }
}

package org.autopotato.linkedinclonebackend.services;

import lombok.Value;
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

    public Connection create(long senderId, long receiverId) {
        Connection connection = new Connection(
            new Person(senderId),
            new Person(receiverId)
        );

        return repo.save(connection);
    }
}

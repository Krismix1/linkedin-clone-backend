package org.autopotato.linkedinclonebackend.repositories;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.springframework.stereotype.Component;

@Component
public class MockConnectionRequestRepository
    implements CrudRepository<ConnectionRequest, Long> {
    Map<Long, ConnectionRequest> db = new LinkedHashMap<>();
    private static long lastId;

    @Override
    public <S extends ConnectionRequest> S save(S entity) {
        if (entity.getId() == 0) {
            entity.setId(++lastId);
        }
        db.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<ConnectionRequest> findById(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public boolean existsById(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return db.containsKey(id);
    }

    @Override
    public Iterable<ConnectionRequest> findAll() {
        return db.values();
    }

    @Override
    public long count() {
        return db.size();
    }

    @Override
    public void deleteById(Long id)
        throws NoSuchElementException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (db.remove(id) == null) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(ConnectionRequest entity)
        throws NoSuchElementException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        if (db.remove(entity.getId()) == null) {
            throw new NoSuchElementException();
        }
    }
}

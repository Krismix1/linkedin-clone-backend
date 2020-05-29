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
    public Optional<ConnectionRequest> findById(Long aLong) {
        return Optional.ofNullable(db.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return db.containsKey(aLong);
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
    public void deleteById(Long aLong) throws NoSuchElementException {
        if (db.remove(aLong) == null) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(ConnectionRequest entity) {
        db.remove(entity.getId());
    }
}

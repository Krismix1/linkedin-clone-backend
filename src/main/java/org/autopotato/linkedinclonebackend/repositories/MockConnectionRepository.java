package org.autopotato.linkedinclonebackend.repositories;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.autopotato.linkedinclonebackend.model.Connection;
import org.springframework.stereotype.Component;

@Component
public class MockConnectionRepository implements CrudRepository<Connection, Long> {
    private Map<Long, Connection> db = new LinkedHashMap<>();

    @Override
    public <S extends Connection> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Connection> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Connection> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {}

    @Override
    public void delete(Connection entity) {}
}

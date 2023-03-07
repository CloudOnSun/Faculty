package main.java.server.repository.memoryRepo;

import main.java.server.domain.Entity;
import main.java.server.exceptions.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The repo to store the entities in memory.
 * @param <ID> identity's id
 * @param <E> identity's type
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected final Map<ID, E> entities;

    public InMemoryRepository() {
        entities = new HashMap<>();
    }

    /**
     * Finds the entity with the given id
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the needed entity
     */
    @Override
    public E findOne(ID id) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        return entities.get(id);
    }

    /**
     * Finds all the entities saved in memory
     *
     * @return a list of entities
     */
    @Override
    public List<E> findAll() {
        return entities.values().stream().toList();
    }

    /**
     * Saves the entity in the memory
     *
     * @param entity the entity to save
     *               entity must be not null
     * @return the saved entity or null if it already exists
     */
    @Override
    public E save(E entity) {
        if (entity == null) throw new IllegalArgumentException("Entity must not be null");
        if (entities.get(entity.getId()) != null) return null;
        else entities.put(entity.getId(), entity);
        return entity;
    }

    /**
     * Deletes and entity with the given id
     *
     * @param id the id of the entity to delete
     *           id must be not null
     * @return the deleted entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        if (entities.get(id) == null) return null;
        var e = entities.remove(id);
        return e;
    }
}

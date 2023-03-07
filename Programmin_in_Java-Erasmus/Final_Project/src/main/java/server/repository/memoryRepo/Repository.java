package main.java.server.repository.memoryRepo;

import main.java.server.domain.Entity;
import main.java.server.exceptions.ValidationException;

import java.util.List;

public interface Repository <ID, E extends Entity<ID>> {

    /***
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     */
    E findOne(ID id);

    /***
     *
     * @return all entities
     */
    List<E> findAll();

    /***
     *
     * @param entity
     *         entity must be not null
     * @return the saved entity - if the given entity is saved
     *         otherwise returns null (id already exists)
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    E save(E entity);


    /***
     *
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    E delete(ID id);


}
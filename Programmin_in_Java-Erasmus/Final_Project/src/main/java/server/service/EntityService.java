package main.java.server.service;

import main.java.server.domain.Entity;
import main.java.server.exceptions.ValidationException;
import main.java.server.exceptions.ExistingException;

import java.util.List;

public interface EntityService<ID, E extends Entity<ID>> {
    /***
     * Adds the entity
     * @param e the entity to add
     * @throws ValidationException if the entity is invalid
     * @throws ExistingException if the entity already exists
     */
    void addEntity(E e);

    /***
     * Remove the entity with the given id
     * @param id the given id of the entity
     * @return the id
     * @throws ExistingException if the entity doesn't exist
     */
    E deleteEntity(ID id);

    /**
     * Gets all the entites
     * @return a list with the entities
     */
    List<E> getAll();

    /***
     * finds the entity with the given id
     * @throws ExistingException if the entity doesn't exist
     */
    E findOne(ID id);

}
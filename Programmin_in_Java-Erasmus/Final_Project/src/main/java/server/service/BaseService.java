package main.java.server.service;

import main.java.server.domain.Entity;
import main.java.server.exceptions.ExistingException;
import main.java.server.exceptions.ValidationException;
import main.java.server.repository.memoryRepo.Repository;
import main.java.server.validators.Validator;
import main.java.server.validators.ValidatorFactory;
import main.java.server.validators.ValidatorType;

import java.util.List;

public class BaseService<ID, E extends Entity<ID>> implements EntityService<ID, E> {
    private final Validator<E> validator;
    protected final Repository<ID, E> repository;

    /**
     * This gets the needed validator by using the factory pattern
     * @param validatorType the type of the validator we'll use
     * @param repository the repository used in the service
     */
    public BaseService(ValidatorType validatorType, Repository<ID, E> repository) {
        ValidatorFactory instance = ValidatorFactory.getInstance();
        Validator validator = instance.createValidator(validatorType);
        this.validator = validator;
        this.repository = repository;
    }

    /**
     * Adds the entity in the repo after valdiation it
     * @param e the entity to save
     * @throws ValidationException if the user is invalid
     * @throws ExistingException if the user's id already exists
     */
    @Override
    public void addEntity(E e){
        validator.validate(e);
        repository.save(e);
    }

    /**
     * Removes an entity by its id
     * @param id  the id of the entity to remove
     * @return the deleted entity
     * @throws ExistingException if the user doesn't exist
     */
    @Override
    public E deleteEntity(ID id) {
        return repository.delete(id);
    }

    /**
     * Gets a list of all entities
     * @return the list with all entities
     */
    @Override
    public List<E> getAll() {
        return repository.findAll();
    }

    /***
     * finds the entity with the given id
     * @throws ExistingException if the entity doesn't exist
     */
    @Override
    public E findOne(ID id) {
        return repository.findOne(id);

    }

}

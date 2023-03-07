package main.java.server.repository.fileRepo;

import main.java.server.domain.Entity;
import main.java.server.domain.User;
import main.java.server.exceptions.ExistingException;
import main.java.server.exceptions.ValidationException;
import main.java.server.repository.memoryRepo.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract class that has the needed method for getting entities from
 * a file and writing them back
 *
 * @param <ID> the type of the id of the entity
 * @param <E> the type of the entity
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    private final String fileName;


    /**
     * First we set the filename we'll use and the load the data from it
     *
     * @param fileName
     */
    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    /**
     * Gets the data from the file
     * Iterates over each line and used the abstract method extractEntity
     * to map the String line to the needed entity
     */
    private void loadData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E e = extractEntity(attributes);
                if (e != null)
                    super.save(e);
            }
        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets an entity and uses the entityToString method
     * to map the entity to a String we'll put in the file
     *
     * @param e the entity to write in the file
     */
    private void writeEntityToFile(E e) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
            bufferedWriter.write(entityToString(e));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Clear the file we have in the repo
     */
    private void clearFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
            bufferedWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Saves the entity in the repository and then writes it in the file
     *
     * @param entity - the entity to save and write in file
     * @return the saved entity
     *          null if the entity already exists
     * @throws IllegalArgumentException if the entity to save is null
     */
    @Override
    public E save(E entity) {
        if (super.save(entity) == null)
            return null;
        writeEntityToFile(entity);
        return entity;
    }

    /***
     *  removes the entity with the specified id and rewrites the entire file
     * @param id - the id of the entity
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if (e == null) return null;
        clearFile();
        super.findAll().forEach(this::writeEntityToFile);
        return e;
    }

    /**
     * abstract method to get the entity from a string
     *
     * @param attributes - a list of string representing the arguments of the entity class
     * @return the entity
     */
    abstract E extractEntity(List<String> attributes);

    /**
     * abstract method to get the string from an entity
     *
     * @param e - the entity to map to a string
     * @return the string
     */
    abstract String entityToString(E e);

}
package main.java.server.repository.fileRepo;

import main.java.server.domain.Entity;
import main.java.server.domain.User;
import main.java.server.exceptions.ValidationException;
import main.java.server.exceptions.ExistingException;

import java.util.List;

public class FileUserRepo extends AbstractFileRepository<Long, User> {
    private long maxId;

    public FileUserRepo(String fileName) {
        super(fileName);
        maxId = findAll().stream()
                .mapToLong(Entity::getId)
                .max().orElse(0);
    }

    /***
     * Saves the user in the repository and then writes it in the file
     *
     * @param user - the user to save and write in file
     * @return the saved user
     * @throws IllegalArgumentException if the user to save is null
     * @throws ExistingException if the user already exists
     */
    @Override
    public User save(User user)  {
        if (user.getId() == null) {
            maxId++;
            user.setId(maxId);
        } else {
            maxId = Math.max(maxId, user.getId());
        }
        var u = super.save(user);
        if(u == null){
            throw new ExistingException("User already exists");
        }
        return u;
    }


    /***
     *  removes the user with the specified id and rewrites the entire file
     * @param id - id of the wanted user
     * @return the removed user
     * @throws ExistingException if the user doesn't exist
     */
    @Override
    public User delete(Long id){
        var user = super.delete(id);
        if(user == null){
            throw new ExistingException("User doesn't exist");
        }
        return user;
    }

    /***
     *
     * @param id -the id of the user to be returned
     * @return the entity with the specified id
     * @throws ExistingException if the user doesn't exist
     */
    @Override
    public User findOne(Long id){
        var user = super.findOne(id);
        if (user == null) {
            throw new ExistingException("User doesn't exist");
        }
        return user;
    }

    /**
     * Maps the saved string to the user
     *
     * @param attributes - a list of string representing the arguments of the user class
     * @return the converted user
     */
    @Override
    User extractEntity(List<String> attributes) {
        try {
            long id = Long.parseLong(attributes.get(0));
            return new User(
                    id,
                    attributes.get(1),
                    attributes.get(2)
            );
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * Maps the user to the saved string
     *
     * @param user - the user to convert
     * @return the string converted
     */
    @Override
    String entityToString(User user) {
        return String.format("%s;%s;%s", user.getId().toString(), user.getName(), user.getEmail());
    }

}

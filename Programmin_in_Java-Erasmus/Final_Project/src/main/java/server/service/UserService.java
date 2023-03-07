package main.java.server.service;

import main.java.server.domain.Friendship;
import main.java.server.domain.User;
import main.java.server.exceptions.ValidationException;
import main.java.server.exceptions.ExistingException;
import main.java.server.repository.memoryRepo.Repository;
import main.java.server.validators.ValidatorType;

public class UserService extends BaseService<Long, User> {
    private final Repository<Long, Friendship> friendshipRepo;

    public UserService(Repository<Long, User> repository, Repository<Long, Friendship> friendshipRepo) {
        super(ValidatorType.USER, repository);
        this.friendshipRepo = friendshipRepo;
    }

    /***
     * creates a new instance of the user and adds it to the repo
     * @param email the email of the user
     * @param name the name of the user
     * @throws ValidationException if the user is invalid
     * @throws ExistingException if the entity exists
     */

    public Long addUser(String name, String email) {
        User user = EntitySingleton.getInstance().getUser(name, email);
        super.addEntity(user);
        return user.getId();
    }

    /***
     * creates a new instance of the user and adds it to the repo
     * @param ID the id of the user
     * @param email the email of the user
     * @param name the name of the user
     * @throws ValidationException if the user is invalid
     * @throws ExistingException if the entity exists
     */
    public void addUserID(Long ID, String name, String email) {
        //!!!!!!
        //User user = EntitySingleton.getInstance().getUserID(ID, name, email);
        User user = EntitySingleton.getInstance().getUserID(ID, email, name);
        super.addEntity(user);
    }

    /***
     * Removes an user and the removes all the friendships related to him
     * @param userId the id of the removed user
     * @return the removed user
     * @throws ExistingException if the entity doesn't exist
     */
    @Override
    public User deleteEntity(Long userId) {
        User user = super.deleteEntity(userId);
        friendshipRepo.findAll()
                .forEach(friendship -> {
                    if (friendship.getIdUser1().equals(userId) || friendship.getIdUser2().equals(userId)) {
                        friendshipRepo.delete(friendship.getId());
                    }
                });
        return user;
    }

}


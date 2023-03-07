package main.java.server.service;

import main.java.server.domain.Friendship;
import main.java.server.domain.User;
import main.java.server.exceptions.ExistingException;
import main.java.server.exceptions.ValidationException;
import main.java.server.repository.memoryRepo.Repository;
import main.java.server.validators.ValidatorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendshipService extends BaseService<Long, Friendship>{
    private final Repository<Long, Friendship> friendshipRepo;
    private final Repository<Long, User> userRepo;

    public FriendshipService(Repository<Long, Friendship> repository, Repository<Long, User> userRepository) {
        super(ValidatorType.FRIENDSHIP, repository);
        System.out.println(repository.getClass());
        friendshipRepo = repository;
        this.userRepo = userRepository;
    }

    /**
     * Returns a map with all users and their friends - used for printing the friendships
     *
     * @return a map where with keys as ids of users and values as their friends
     */
    public Map<User, List<User>> getAllUsersFriends() {
        HashMap<User, List<User>> map = new HashMap<>();
        List<Friendship> all = getAll();
        all.forEach(friendship -> {
            User user1 = userRepo.findOne(friendship.getIdUser1());
            User user2 = userRepo.findOne(friendship.getIdUser2());

            if (user1 != null)
                map.computeIfAbsent(user1, k -> new ArrayList<>());
            if (user2 != null)
                map.computeIfAbsent(user2, k -> new ArrayList<>());

            if (user1 != null && user2 != null)
                map.get(user1).add(userRepo.findOne(friendship.getIdUser2()));
            if (user2 != null && user1 != null)
                map.get(user2).add(userRepo.findOne(friendship.getIdUser1()));
        });
        return map;
    }

    /***
     * Creates a new Friendship between two users and adds it to the repo
     *
     * @param id - the id of the friendship
     * @param id1 the id of the first user
     * @param id2 the id of the second user
     * @param friendsFr - date and time since they are friends
     * @throws ValidationException if the relation is invalid
     * @throws ExistingException if the friendship already exists
     */
    public void addFriends(Long id, Long id1, Long id2,String friendsFr) {
        Friendship friendShip = EntitySingleton.getInstance().getFriendShip(id, id1, id2,friendsFr);
        super.addEntity(friendShip);
    }

    /***
     * Creates a new Friendship between two users and adds it to the repo
     *
     * @param idu1 the id of the first user
     * @param idu2 the id of the second user
     * @param friendsFr - date and time since they are friends
     * @throws ValidationException if the relation is invalid
     * @throws ExistingException if the friendship already exists
     */
    public void addFriends(Long idu1, Long idu2, String friendsFr) {
        Friendship friendship = EntitySingleton.getInstance().getFriendShip(idu1, idu2, friendsFr);
        super.addEntity(friendship);
    }

}

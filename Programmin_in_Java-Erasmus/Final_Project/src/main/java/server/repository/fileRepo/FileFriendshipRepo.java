package main.java.server.repository.fileRepo;

import main.java.server.domain.Entity;
import main.java.server.domain.Friendship;
import main.java.server.exceptions.ExistingException;
import main.java.server.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

public class FileFriendshipRepo extends AbstractFileRepository<Long, Friendship> {
    private final FileUserRepo fileUserRepo;
    private long maxId;

    public FileFriendshipRepo(String fileName, FileUserRepo fileUserRepo) {
        super(fileName);
        this.fileUserRepo = fileUserRepo;
        maxId = findAll().stream()
                .mapToLong(Entity::getId)
                .max().orElse(0);
    }

    /***
     * Saves the friendship in the repository and then writes it in the file
     *
     * @param friendship - the friendship to save and write in file
     * @return the saved friendship
     * @throws IllegalArgumentException if the friendship to save is null
     * @throws ExistingException if the friendship already exists
     *                              or one of the users or both do not exist
     */
    @Override
    public Friendship save(Friendship friendship) {
        if (friendship.getId() == null) {
            maxId++;
            friendship.setId(maxId);
        } else {
            maxId = Math.max(maxId, friendship.getId());
        }
        var u1 = fileUserRepo.findOne(friendship.getIdUser1());
        var u2 = fileUserRepo.findOne(friendship.getIdUser2());
        findAll().forEach(a -> {
            if(a.getIdUser1() == friendship.getIdUser1() &&
                a.getIdUser2() == friendship.getIdUser2())
                throw new ExistingException("Friendship already exists");
        });
        var f = super.save(friendship);
        if (f == null){
            throw new ExistingException("Friendship already exists");
        }
        return f;
    }

    /***
     *  removes the friendship with the specified id and rewrites the entire file
     * @param id - the id of the friendship
     * @return the removed friendship
     * @throws ExistingException if the friendship doesn't exist
     */
    @Override
    public Friendship delete(Long id){
        var fr = super.delete(id);
        if (fr == null){
            throw new ExistingException("Friendship doesn't exist");
        }
        return fr;
    }

    /***
     *
     * @param id -the id of the friendship to be returned
     * @return the friendship with the specified id
     * @throws ExistingException if the friendship doesn't exist
     */
    @Override
    public Friendship findOne(Long id){
        var fr = super.findOne(id);
        if (fr == null){
            throw new ExistingException("Friendship doesn't exist");
        }
        return fr;
    }

    /**
     * Maps the saved string to the friendship
     * @param attributes - a list of string representing the arguments of the friendship class
     * @return the converted friendship
     */
    @Override
    Friendship extractEntity(List<String> attributes) {
        try {
            long id = Long.parseLong(attributes.get(0));
            long id1 = Long.parseLong(attributes.get(1));
            long id2 = Long.parseLong(attributes.get(2));
            String frFrom = attributes.get(3);
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            LocalDateTime since = LocalDateTime.parse(frFrom, formatter);
            return new Friendship(id, id1, id2, since);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Maps the friendship to the saved string
     * @param friendship - the friendship to convert
     * @return the string converted
     */
    @Override
    String entityToString(Friendship friendship) {
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm:ss]");
        String frfrom=dtf.format(friendship.getFriendsFrom());
        return String.format("%s;%s;%s;%s", friendship.getId(), friendship.getIdUser1(), friendship.getIdUser2(), frfrom);
    }

}

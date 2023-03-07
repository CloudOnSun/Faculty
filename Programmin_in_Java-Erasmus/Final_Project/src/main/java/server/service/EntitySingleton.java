package main.java.server.service;

import main.java.server.domain.Friendship;
import main.java.server.domain.User;
import main.java.server.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class EntitySingleton {
    private static EntitySingleton instance = null;

    private EntitySingleton() {
    }

    /**
     * Singleton Pattern
     * @return an entity of this type
     */
    public static EntitySingleton getInstance() {
        if (instance == null) instance = new EntitySingleton();
        return instance;
    }

    /**
     * creates an user
     * @param email the new user's email
     * @param name the new user's name
     * @return the new user
     */
    public User getUser(String name, String email) {
        return new User(name, email);
    }

    /**
     * creates an user
     * @param ID the new user's id
     * @param email the new user's email
     * @param name the new user's name
     * @return the new user
     */
    public User getUserID(Long ID,String email, String name) {
        return new User(ID,name, email);
    }

    /**
     * creates a new instance of Friendship
     * @param id the id of the Friendship created
     * @param id1 the id of the first user
     * @param id2 the id of the second user
     * @param frFrom the date since they were friends
     * @return the created Friendship
     * @throws ValidationException if date is invalid
     */
    public Friendship getFriendShip(long id, long id1, long id2, String frFrom){
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            LocalDateTime since=LocalDateTime.parse(frFrom,formatter);
            return new Friendship(id, id1, id2,since);
        } catch (DateTimeParseException ex) {
            throw new ValidationException("invalid date");
        }
    }

    public Friendship getFriendShip(long id1, long id2, String frFrom){
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            LocalDateTime since=LocalDateTime.parse(frFrom,formatter);
            return new Friendship(id1, id2, since);
        } catch (DateTimeParseException ex) {
            throw new ValidationException("invalid date");
        }
    }

}

package main.java.server.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * a class that holds the user's information.
 */
public class  User extends Entity<Long> implements Serializable {
    private String name;
    private String email;

    private List<String> friends;

    public User(Long ID, String name, String email) {
        this.setId(ID);
        this.name = name;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    public User(String name, String email) {
        this.email = email;
        this.name = name;
    }

    /**
     * getter for user's name
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for user's email
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User: id=" + getId()+
                ", name='" + name + '\'' +
                ", email='" + email + '\'' ;
    }

    /**
     * the definition of when 2 users are considered equals
     * @param o the user to compare to
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.getId() == user.getId() && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), email);
    }
}
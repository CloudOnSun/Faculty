package main.java.server.domain;

import java.io.Serializable;

/**
 * a generic class to use in our specific objects
 * @param <ID>  generic member - T the type of the id's (String, Long etc)
 */
public class Entity <ID> implements Serializable {
    private ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }

}
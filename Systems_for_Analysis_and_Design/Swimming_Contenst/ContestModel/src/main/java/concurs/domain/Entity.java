package concurs.domain;

import java.io.Serializable;

public class Entity<Tid>  implements Serializable {
    protected Tid ID;

    public void setID(Tid ID) {
        this.ID = ID;
    }

    public Tid getID(){
        return ID;
    }
}

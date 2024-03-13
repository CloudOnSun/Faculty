package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table (name = "Teatru")
public class Teatru extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    public String nume;
    @OneToMany(targetEntity = Spectacol.class, mappedBy = "id")
    public List<Spectacol> spectacole;

    public Teatru(String nume) {
        this.nume = nume;
        this.spectacole = new ArrayList<>();
    }

    public Teatru() {

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Spectacol> getSpectacole() {
        return spectacole;
    }

    public void basicAddToSpectacole(Spectacol spectacol) {
        this.spectacole.add(spectacol);
    }

    public void addToSpectacole(Spectacol spectacol) {
        this.basicAddToSpectacole(spectacol);
        spectacol.setTeatru(this);
    }

    public void basicRemoveFromSpectacole(Spectacol spectacol) {
        this.spectacole.remove(spectacol);
    }

    public void removeFromSpectacole(Spectacol spectacol) {
        this.basicRemoveFromSpectacole(spectacol);
        spectacol.setTeatru(null);
    }

    @Override
    public String toString() {
        return "";
    }
}

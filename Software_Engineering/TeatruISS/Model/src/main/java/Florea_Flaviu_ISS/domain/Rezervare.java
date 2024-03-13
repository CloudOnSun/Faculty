package Florea_Flaviu_ISS.domain;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table (name = "Rezervare")
public class Rezervare extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    public Integer code;
    @ManyToOne
    @JoinColumn(name = "spectator", referencedColumnName = "id", nullable = false)
    public Spectator spectator;
    @ManyToOne
    @JoinColumn(name = "spectacol", referencedColumnName = "id", nullable = false)
    public Spectacol spectacol;
    @OneToMany(targetEntity = Loc.class, mappedBy="id")
    public List<Loc> locuri;

    public Rezervare(Integer code, Spectator spectator, Spectacol spectacol) {
        this.code = code;
        this.spectator = spectator;
        this.spectacol = spectacol;
        this.locuri = new ArrayList<>();
    }

    public Rezervare() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Spectator getSpectator() {
        return spectator;
    }

    public void setSpectator(Spectator spectator) {
        this.spectator = spectator;
    }

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    public List<Loc> getLocuri() {
        return locuri;
    }

    public void basicAddToLocuri(Loc loc) {
        this.locuri.add(loc);
    }

    public void addToLocuri(Loc loc) {
        this.basicAddToLocuri(loc);
        loc.setRezervare(this);
    }

    public void basicRemoveFromLocuri(Loc loc) {
        this.locuri.remove(loc);
    }

    public void removeFromLocuri(Loc loc) {
        this.basicRemoveFromLocuri(loc);
        loc.setRezervare(null);
    }

    @Override
    public String toString() {
        return "";
    }
}

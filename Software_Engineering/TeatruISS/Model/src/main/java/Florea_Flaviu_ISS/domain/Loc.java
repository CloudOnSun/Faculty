package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.Objects;


import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table (name = "Loc")
public class Loc extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    public Integer loja;
    public Integer rand;
    public Integer loc;

    @ManyToOne
    @JoinColumn(name = "rezervare", referencedColumnName = "id")
    public Rezervare rezervare;

    @ManyToOne
    @JoinColumn(name = "spectacol", referencedColumnName = "id", nullable = false)
    public Spectacol spectacol;

    public Loc(Integer loja, Integer rand, Integer loc, Spectacol spectacol) {
        this.loja = loja;
        this.rand = rand;
        this.loc = loc;
        this.spectacol = spectacol;
    }

    public Loc() {

    }

    public Integer getLoja() {
        return loja;
    }

    public void setLoja(Integer loja) {
        this.loja = loja;
    }

    public Integer getRand() {
        return rand;
    }

    public void setRand(Integer rand) {
        this.rand = rand;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Rezervare getRezervare() {
        return rezervare;
    }

    public void setRezervare(Rezervare rezervare) {
        this.rezervare = rezervare;
    }

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    @Override
    public String toString() {
        return "";
    }
}

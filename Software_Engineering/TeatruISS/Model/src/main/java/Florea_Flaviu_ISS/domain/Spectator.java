package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table (name = "Spectator")
public class Spectator extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    public String nume;
    public String prenume;
    public String email;

    @OneToMany(targetEntity = Rezervare.class, mappedBy = "id")
    public List<Rezervare> rezervari;

    public Spectator(String nume, String prenume, String email) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.rezervari = new ArrayList<>();
    }

    public Spectator() {

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Rezervare> getRezervari() {
        return rezervari;
    }

    public void basicAddToRezervari(Rezervare rezervare) {
        this.rezervari.add(rezervare);
    }

    public void addToRezervari(Rezervare rezervare) {
        this.basicAddToRezervari(rezervare);
        rezervare.setSpectator(this);
    }

    public void basicRemoveFromRezervari(Rezervare rezervare) {
        this.rezervari.remove(rezervare);
    }

    public void removeFromRezervari(Rezervare rezervare) {
        this.basicRemoveFromRezervari(rezervare);
        rezervare.setSpectator(null);
    }

    @Override
    public String toString() {
        return "";
    }
}

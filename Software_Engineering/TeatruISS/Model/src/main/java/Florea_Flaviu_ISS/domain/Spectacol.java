package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table (name = "Spectacol")
public class Spectacol extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    String titlu;
    @ManyToOne
    @JoinColumn(name = "dateTime", referencedColumnName = "id", nullable = false)
    public DateTime dateTime;
    public String regizor;
    public Genuri gen;
    @ManyToOne
    @JoinColumn(name = "teatru", referencedColumnName = "id", nullable = false)
    public Teatru teatru;

    @ManyToOne
    @JoinColumn(name = "manager", referencedColumnName = "id", nullable = false)
    public Manager manager;

    @OneToMany(targetEntity = Rezervare.class, mappedBy = "id")
    public List<Rezervare> rezervari;

    @OneToMany(targetEntity = Loc.class, mappedBy = "id")
    public List<Loc> locuri;

    public Spectacol(String titlu, DateTime dateTime, String regizor, Genuri gen, Manager manager) {
        this.titlu = titlu;
        this.dateTime = dateTime;
        this.regizor = regizor;
        this.gen = gen;
        this.manager = manager;
        this.rezervari = new ArrayList<>();
        this.locuri = new ArrayList<>();
    }

    public Spectacol() {

    }

    public Teatru getTeatru() {
        return teatru;
    }

    public void setTeatru(Teatru teatru) {
        this.teatru = teatru;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getRegizor() {
        return regizor;
    }

    public void setRegizor(String regizor) {
        this.regizor = regizor;
    }

    public Genuri getGen() {
        return gen;
    }

    public void setGen(Genuri gen) {
        this.gen = gen;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Rezervare> getRezervari() {
        return rezervari;
    }

    public List<Loc> getLocuri() {
        return locuri;
    }

    public void basicAddToRezervari(Rezervare rezervare) {
        this.rezervari.add(rezervare);
    }

    public void addToRezervari(Rezervare rezervare) {
        this.basicAddToRezervari(rezervare);
        rezervare.setSpectacol(this);
    }

    public void basicRemoveFromRezervari(Rezervare rezervare) {
        this.rezervari.remove(rezervare);
    }

    public void removeFromRezervari(Rezervare rezervare) {
        this.basicRemoveFromRezervari(rezervare);
        rezervare.setSpectacol(null);
    }

    public void basicAddToLocuri(Loc loc) {
        this.locuri.add(loc);
    }

    public void addToLocuri(Loc loc) {
        this.basicAddToLocuri(loc);
        loc.setSpectacol(this);
    }

    @Override
    public String toString() {
        return "";
    }

}

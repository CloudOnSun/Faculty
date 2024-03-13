package Florea_Flaviu_ISS.domain;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "Manager")
public class Manager extends Florea_Flaviu_ISS.domain.Entity<Integer> implements Serializable {

    private String email;
    private String password;
    @OneToMany(targetEntity = Spectacol.class, mappedBy = "id")
    public List<Spectacol> spectacole;

    public Manager(String email, String password) {
        this.email = email;
        this.password = password;
        this.spectacole = new ArrayList<>();
    }

    public Manager() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Spectacol> getSpectacole() {
        return spectacole;
    }

    public void basicAddToSpectacole(Spectacol spectacol) {
        spectacole.add(spectacol);
    }

    public void addToSpectacole(Spectacol spectacol) {
        this.basicAddToSpectacole(spectacol);
        spectacol.setManager(this);
    }

    public void basicRemoveFromSpectacole(Spectacol spectacol) {
        this.spectacole.remove(spectacol);
    }

    public void removeFromSpectacole(Spectacol spectacol) {
        this.basicRemoveFromSpectacole(spectacol);
        spectacol.setManager(null);
    }

    @Override
    public String toString() {
        return "";
    }
}

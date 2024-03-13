package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RezervareDTO implements Serializable {

    private String nume;
    private String prenume;
    private String email;
    private Spectacol spectacol;
    private List<Loc> locuri;

    public RezervareDTO(String nume, String prenume, String email, Spectacol spectacol) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.spectacol = spectacol;
        this.locuri = new ArrayList<>();
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

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    public List<Loc> getLocuri() {
        return locuri;
    }

    public void setLocuri(List<Loc> locuri) {
        this.locuri = locuri;
    }
}

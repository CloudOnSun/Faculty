package Florea_Flaviu_ISS.domain;

import java.io.Serializable;

public class ManagerLogInDTO implements Serializable {
    public String email;
    public String password;

    public ManagerLogInDTO(String email, String password) {
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return "";
    }
}

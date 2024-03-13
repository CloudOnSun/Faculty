package concurs.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Admin")
public class Admin extends concurs.domain.Entity<Integer> implements Serializable {

    private String email;
    private String password;
    private int officeID;

    public Admin() {

    }

    @Override
    public String toString() {
        return "Admin{" +
                "ID=" + ID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", officeID=" + officeID +
                '}';
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Override
    public Integer getID() {
        return super.getID();
    }

    @Override
    public void setID(Integer ID) {
        super.setID(ID);
    }

    public Admin(String email, String password, int officeID) {
        this.email = email;
        this.password = password;
        this.officeID = officeID;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public int getOfficeID() {
        return officeID;
    }

    private void setOfficeID(int officeID) {
        this.officeID = officeID;
    }
}

package concurs.domain;

import java.io.Serializable;

public class Contestant extends Entity<Integer> implements Serializable {

    private String name;
    private int age;

    @Override
    public String toString() {
        return "Contestant{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Contestant(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    private void setAge(int age) {
        this.age = age;
    }
}

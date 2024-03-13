package concurs.domain;

import java.io.Serializable;

public class ContestantWithRacesDTO implements Serializable {

    private String name;
    private Integer age;
    private String races;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRaces() {
        return races;
    }

    public void setRaces(String races) {
        this.races = races;
    }

    public ContestantWithRacesDTO(String name, Integer age, String races) {
        this.name = name;
        this.age = age;
        this.races = races;
    }
}

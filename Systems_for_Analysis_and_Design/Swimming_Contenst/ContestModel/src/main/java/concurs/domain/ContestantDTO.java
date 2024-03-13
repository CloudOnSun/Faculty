package concurs.domain;

import java.io.Serializable;
import java.util.List;

public class ContestantDTO implements Serializable {

    private String name;
    private Integer age;
    private List<SwimmingRace> races;

    public ContestantDTO(String name, Integer age, List<SwimmingRace> races) {
        this.name = name;
        this.age = age;
        this.races = races;
    }

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

    public List<SwimmingRace> getRaces() {
        return races;
    }

    public void setRaces(List<SwimmingRace> races) {
        this.races = races;
    }
}

package concurs.domain;

import java.io.Serializable;

public class TakesPart extends Entity<Integer> implements Serializable {

    private Contestant contestant;

    private SwimmingRace race;

    public TakesPart(Contestant contestant, SwimmingRace race) {
        this.contestant = contestant;
        this.race = race;
    }

    @Override
    public String toString() {
        return "TakesPart{" +
                "ID=" + ID +
                ", contestant=" + contestant +
                ", race=" + race +
                '}';
    }

    public Contestant getContestant() {
        return contestant;
    }

    private void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public SwimmingRace getRace() {
        return race;
    }

    private void setRace(SwimmingRace race) {
        this.race = race;
    }
}

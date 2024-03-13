package concurs.domain;

import java.io.Serializable;

public class SwimmingRace extends Entity<Integer> implements Serializable {
    private String distance;
    private String style;

    public SwimmingRace(String distance, String style) {
        this.distance = distance;
        this.style = style;
    }

    public SwimmingRace() {
        this.distance = "";
        this.style = "";
        this.setID(0);
    }

    @Override
    public String toString() {
        return "SwimmingRace{" +
                "ID=" + ID +
                ", distance='" + distance + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

    public String getDistance() {
        return distance;
    }

    private void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    private void setStyle(String style) {
        this.style = style;
    }
}

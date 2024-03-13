package concurs.domain;

import java.io.Serializable;

public class RaceDTO implements Serializable {
    private String distance;
    private String style;

    public RaceDTO(String distance, String style) {
        this.distance = distance;
        this.style = style;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

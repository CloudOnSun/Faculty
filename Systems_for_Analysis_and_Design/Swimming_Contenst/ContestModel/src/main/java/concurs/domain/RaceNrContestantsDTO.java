package concurs.domain;

import java.io.Serializable;

public class RaceNrContestantsDTO implements Serializable {

    private String distance;
    private String style;
    private Integer nrContestants;

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

    public void setNrContestants(Integer nrContestants) {
        this.nrContestants = nrContestants;
    }

    public Integer getNrContestants() {
        return nrContestants;
    }

    public RaceNrContestantsDTO(String distance, String style, Integer nrContestants) {
        this.distance = distance;
        this.style = style;
        this.nrContestants = nrContestants;
    }
}

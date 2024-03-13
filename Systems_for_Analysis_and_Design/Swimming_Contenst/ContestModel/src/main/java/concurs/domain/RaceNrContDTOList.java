package concurs.domain;

import java.io.Serializable;
import java.util.List;

public class RaceNrContDTOList implements Serializable {

    private List<RaceNrContestantsDTO> raceNrContestantsDTOList;

    public RaceNrContDTOList(List<RaceNrContestantsDTO> raceNrContestantsDTOList) {
        this.raceNrContestantsDTOList = raceNrContestantsDTOList;
    }

    public List<RaceNrContestantsDTO> getRaceNrContestantsDTOList() {
        return raceNrContestantsDTOList;
    }

    public void setRaceNrContestantsDTOList(List<RaceNrContestantsDTO> raceNrContestantsDTOList) {
        this.raceNrContestantsDTOList = raceNrContestantsDTOList;
    }
}

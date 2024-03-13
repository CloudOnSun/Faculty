package concurs.domain;

import java.io.Serializable;
import java.util.List;

public class ContWithRaceDTOList implements Serializable {

    private List<ContestantWithRacesDTO> contestantWithRacesDTOList;

    public ContWithRaceDTOList(List<ContestantWithRacesDTO> contestantWithRacesDTOList) {
        this.contestantWithRacesDTOList = contestantWithRacesDTOList;
    }

    public List<ContestantWithRacesDTO> getContestantWithRacesDTOList() {
        return contestantWithRacesDTOList;
    }

    public void setContestantWithRacesDTOList(List<ContestantWithRacesDTO> contestantWithRacesDTOList) {
        this.contestantWithRacesDTOList = contestantWithRacesDTOList;
    }
}

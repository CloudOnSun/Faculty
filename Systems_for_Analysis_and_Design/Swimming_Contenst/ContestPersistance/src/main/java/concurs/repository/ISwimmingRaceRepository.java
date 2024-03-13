package concurs.repository;

import concurs.domain.RaceNrContestantsDTO;
import concurs.domain.SwimmingRace;

import java.util.List;

public interface ISwimmingRaceRepository extends Repository<SwimmingRace, Integer> {

    public List<RaceNrContestantsDTO> nrContestantEachRace();

    public List<SwimmingRace> getByContestant(int idCont);

    public SwimmingRace findByDistanceAndStyle(String distance, String style);

    public SwimmingRace[] getRaces();

    public void delete(Integer id);
}

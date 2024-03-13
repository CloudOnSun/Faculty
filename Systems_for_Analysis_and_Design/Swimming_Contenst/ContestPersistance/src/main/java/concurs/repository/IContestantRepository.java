package concurs.repository;

import concurs.domain.Contestant;

import java.util.List;

public interface IContestantRepository extends Repository<Contestant, Integer> {

    public List<Contestant> getByRace(String distance, String style);
}

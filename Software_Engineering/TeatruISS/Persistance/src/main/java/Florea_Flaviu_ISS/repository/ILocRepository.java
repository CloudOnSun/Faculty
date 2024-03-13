package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.Loc;
import Florea_Flaviu_ISS.domain.Spectacol;

import java.util.List;

public interface ILocRepository extends Repository<Loc, Integer> {

    public List<Loc> findBySpectacol(Spectacol spectacol);
}

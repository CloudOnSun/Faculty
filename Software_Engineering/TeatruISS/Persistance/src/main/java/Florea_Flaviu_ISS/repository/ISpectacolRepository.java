package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.Genuri;
import Florea_Flaviu_ISS.domain.Spectacol;

import java.util.List;

public interface ISpectacolRepository extends Repository<Spectacol, Integer> {

    public List<Spectacol> filtrareDupaGen(Genuri gen);

}

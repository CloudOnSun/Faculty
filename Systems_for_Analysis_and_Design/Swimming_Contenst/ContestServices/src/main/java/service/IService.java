package service;

import concurs.domain.*;
//import domain.*;

public interface IService {

    public Admin getAccount(AdminLogInDTO adminLogInDTO, IServiceObserver client) throws ContestException;

    public ContWithRaceDTOList getByRace(RaceDTO race, IServiceObserver client) throws ContestException;

    public RaceNrContDTOList nrContestantEachRace(IServiceObserver client) throws ContestException;

    public SwimmingRace findByDistanceAndStyle(RaceDTO race, IServiceObserver client) throws ContestException;

    public void addContestantToRaces(ContestantDTO contestantDTO, IServiceObserver client) throws ContestException;

    public void logOut(Admin admin, IServiceObserver client) throws ContestException;
}

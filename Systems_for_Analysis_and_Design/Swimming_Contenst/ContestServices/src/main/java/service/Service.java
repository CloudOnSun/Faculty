package service;

import concurs.domain.*;
//import domain.*;
import concurs.repository.IAdminRepository;
import concurs.repository.IContestantRepository;
import concurs.repository.IParticipationRepository;
import concurs.repository.ISwimmingRaceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

public class Service {

    private IAdminRepository adminRepo;
    private IContestantRepository contRepo;
    private IParticipationRepository partRepo;
    private ISwimmingRaceRepository swimRepo;

    private static final Logger logger = LogManager.getLogger();

    public Service(IAdminRepository adminRepo, IContestantRepository contRepo,
                   IParticipationRepository partRepo, ISwimmingRaceRepository swimRepo) {
        logger.info("Creating service with repositories {}, {}, {}, {}", adminRepo, contRepo, partRepo, swimRepo);
        this.adminRepo = adminRepo;
        this.contRepo = contRepo;
        this.partRepo = partRepo;
        this.swimRepo = swimRepo;
    }

    public Admin getAccount(AdminLogInDTO adminLogInDTO) {
        String email = adminLogInDTO.getEmail();
        String password = adminLogInDTO.getPassword();
        logger.traceEntry("Entering getAccount wiht email {} and password {}", email, password);
        var rez = adminRepo.getAccount(email, password);
        logger.traceExit(rez);
        return rez;
    }

    public RaceNrContDTOList nrContestantEachRace() {
        logger.traceEntry("Entering nrContestantEachRace");
        var rez = new RaceNrContDTOList(swimRepo.nrContestantEachRace());

        logger.traceExit(rez);

        return rez;
    }

    public ContWithRaceDTOList getByRace(RaceDTO race) {
        String distance = race.getDistance();
        String style = race.getStyle();
        logger.traceEntry("Entering getByRace with distance {} and style {} ", distance, style);
        var contestants =  contRepo.getByRace(distance, style);
        List<ContestantWithRacesDTO> contestantsWithRaces = new ArrayList<>();
        for (var cont : contestants){
            String races = "";
            var racesList = swimRepo.getByContestant(cont.getID());
            for (var r : racesList) {
                races += r.getDistance();
                races += " " + r.getStyle();
                races += "\n";
            }
            contestantsWithRaces.add(new ContestantWithRacesDTO(cont.getName(), cont.getAge(), races));
        }
        var rez = new ContWithRaceDTOList(contestantsWithRaces);
        logger.traceExit(rez);
        return rez;
    }

    public SwimmingRace findByDistanceAndStyle(RaceDTO race) {
        String distance = race.getDistance();
        String style = race.getStyle();
        logger.traceEntry("findByDistanceAndStyle with distance {} and style {}", distance, style);
        var rez = swimRepo.findByDistanceAndStyle(distance, style);
        logger.traceExit(rez);
        return rez;
    }

    public void addContestantToRaces(ContestantDTO contestantDTO) {
        String name = contestantDTO.getName();
        Integer age = contestantDTO.getAge();
        List<SwimmingRace> races = contestantDTO.getRaces();
        logger.traceEntry("Entering addContestantToRaces with name {} age {} and races {}", name, age, races);
        int id = 0;
        Contestant cont = null;
        boolean done = false;
        while (!done) {
            id = ThreadLocalRandom.current().nextInt(0, 50000 + 1);
            cont = new Contestant(name, age);
            cont.setID(id);
            try {
                contRepo.add(cont);
                done = true;
            }
            catch (Exception ex) {

            }
        }
        for (var race : races) {
            partRepo.add(new TakesPart(cont, race));
        }
        logger.traceExit();
    }
}

package server;

import concurs.domain.*;
//import domain.*;
import concurs.repository.IAdminRepository;
import concurs.repository.IContestantRepository;
import concurs.repository.IParticipationRepository;
import concurs.repository.ISwimmingRaceRepository;
import service.ContestException;
import service.IService;
import service.IServiceObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ContestServiceImpl implements IService {

    private IAdminRepository adminRepo;
    private IContestantRepository contRepo;
    private IParticipationRepository partRepo;
    private ISwimmingRaceRepository swimRepo;
    private Map<Integer, IServiceObserver> loggedClients;

    public ContestServiceImpl(IAdminRepository adminRepo, IContestantRepository contRepo,
                              IParticipationRepository partRepo, ISwimmingRaceRepository swimRepo) {
        this.adminRepo = adminRepo;
        this.contRepo = contRepo;
        this.partRepo = partRepo;
        this.swimRepo = swimRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Admin getAccount(AdminLogInDTO adminLogInDTO, IServiceObserver client) throws ContestException {
        String email = adminLogInDTO.getEmail();
        String password = adminLogInDTO.getPassword();
        var admin = adminRepo.getAccount(email, password);
        if (admin != null) {
            if (loggedClients.get(admin.getID()) != null)
                throw new ContestException("User already logged in!");
            loggedClients.put(admin.getID(), client);
            return admin;
        } else {
            throw new ContestException("Authentication failed!");
        }
    }

    @Override
    public synchronized ContWithRaceDTOList getByRace(RaceDTO race, IServiceObserver client) throws ContestException {
        String distance = race.getDistance();
        String style = race.getStyle();
        var contestants = contRepo.getByRace(distance, style);
        List<ContestantWithRacesDTO> contestantsWithRaces = new ArrayList<>();
        for (var cont : contestants) {
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
        return rez;
    }

    @Override
    public synchronized RaceNrContDTOList nrContestantEachRace(IServiceObserver client) throws ContestException {
        var rez = new RaceNrContDTOList(swimRepo.nrContestantEachRace());
        return rez;
    }

    @Override
    public synchronized SwimmingRace findByDistanceAndStyle(RaceDTO race, IServiceObserver client) throws ContestException {
        String distance = race.getDistance();
        String style = race.getStyle();
        var rez = swimRepo.findByDistanceAndStyle(distance, style);
        return rez;
    }

    @Override
    public synchronized void addContestantToRaces(ContestantDTO contestantDTO, IServiceObserver client) throws ContestException {
        String name = contestantDTO.getName();
        Integer age = contestantDTO.getAge();
        List<SwimmingRace> races = contestantDTO.getRaces();
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
            } catch (Exception ex) {

            }
        }
        for (var race : races) {
            partRepo.add(new TakesPart(cont, race));
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (Integer key : loggedClients.keySet()) {
            IServiceObserver receiver = loggedClients.get(key);
            executor.execute(() -> {
                try {
                    receiver.updateContestants();
                }
                catch (ContestException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }

    }

    @Override
    public synchronized void logOut(Admin admin, IServiceObserver client) throws ContestException {
        IServiceObserver localClient = loggedClients.remove(admin.getID());
        if (localClient == null) {
            throw new ContestException("User is not logged in!");
        }
    }
}

package Florea_Flaviu_ISS.server;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.repository.*;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.IServiceObserver;
import Florea_Flaviu_ISS.service.TeatruException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class TeatruServiceImpl implements IService {

    IManagerRepository managerRepository;
    IDateTimeRepository dateTimeRepository;
    ISpectacolRepository spectacolRepository;
    ILocRepository locRepository;
    ISpectatorRepository spectatorRepository;
    IRezervareRepository rezervareRepository;

    private Set<IServiceObserver> loggedClients;

    public TeatruServiceImpl(IManagerRepository managerRepository, IDateTimeRepository dateTimeRepository, ISpectacolRepository spectacolRepository, ILocRepository locRepository, ISpectatorRepository spectatorRepository, IRezervareRepository rezervareRepository) {
        this.managerRepository = managerRepository;
        this.dateTimeRepository = dateTimeRepository;
        this.spectacolRepository = spectacolRepository;
        this.locRepository = locRepository;
        this.spectatorRepository = spectatorRepository;
        this.rezervareRepository = rezervareRepository;
        this.loggedClients = new HashSet<>();
    }

    @Override
    public synchronized Manager logIn(ManagerLogInDTO managerDTO, IServiceObserver client) throws TeatruException {
        String email = managerDTO.getEmail();
        String password = managerDTO.getPassword();
        var manager = managerRepository.logIn(email, password);
        if (manager != null) {
            loggedClients.add(client);
//            if (loggedClients.get(manager.getId()) != null)
//                throw new TeatruException("User already logged in!");
//            loggedClients.put(manager.getId(), client);
            return manager;
        } else {
            throw new TeatruException("Authentication failed!");
        }
    }

    @Override
    public synchronized List<Spectacol> filtruDupaGen(Genuri gen, IServiceObserver client) throws TeatruException {
        return spectacolRepository.filtrareDupaGen(gen);
    }

    @Override
    public synchronized List<Spectacol> findAllSpectacole(IServiceObserver client) throws TeatruException {
        loggedClients.add(client);
        return spectacolRepository.findAll();
    }

    @Override
    public synchronized void addSpectacol(SpectacolDTO spectacolDTO, IServiceObserver client) throws TeatruException {
        int idDate = 0;
        DateTime date = null;
        boolean done = false;
        while (!done) {
            idDate = ThreadLocalRandom.current().nextInt(0, 500000 + 1);
            date = new DateTime(spectacolDTO.getYear(), spectacolDTO.getMonth(), spectacolDTO.getDay(),
                    spectacolDTO.getHour(), spectacolDTO.getMinute());
            date.setId(idDate);
            try {
                dateTimeRepository.add(date);
                done = true;
            } catch (Exception ex) {

            }
        }

        int idSpec = 0;
        Spectacol spectacol = null;
        done = false;
        while (!done) {
            idSpec = ThreadLocalRandom.current().nextInt(0, 500000 + 1);
            spectacol = new Spectacol(spectacolDTO.getTitlu(), date, spectacolDTO.getRegizor(),
                    spectacolDTO.getGen(), spectacolDTO.getManager());
            spectacol.setId(idSpec);
            spectacol.setTeatru(spectacolDTO.getTeatru());
            try {
                spectacolRepository.add(spectacol);
                done = true;
            } catch (Exception ex) {

            }
        }
        spectacolDTO.getManager().addToSpectacole(spectacol);

        Loc r11 = new Loc(-1, 1, 1, spectacol);
        locRepository.add(r11);
        spectacol.addToLocuri(r11);

        Loc r12 = new Loc(-1, 1, 2, spectacol);
        locRepository.add(r12);
        spectacol.addToLocuri(r12);

        Loc r13 = new Loc(-1, 1, 3, spectacol);
        locRepository.add(r13);
        spectacol.addToLocuri(r13);

        Loc r21 = new Loc(-1, 2, 1, spectacol);
        locRepository.add(r21);
        spectacol.addToLocuri(r21);

        Loc r22= new Loc(-1, 2, 2, spectacol);
        locRepository.add(r22);
        spectacol.addToLocuri(r22);

        Loc r23 = new Loc(-1, 2, 3, spectacol);
        locRepository.add(r23);
        spectacol.addToLocuri(r23);

        Loc l11 = new Loc(1, -1, 1, spectacol);
        locRepository.add(l11);
        spectacol.addToLocuri(l11);

        Loc l12 = new Loc(1, -1, 2, spectacol);
        locRepository.add(l12);
        spectacol.addToLocuri(l12);

        Loc l21 = new Loc(2, -1, 1, spectacol);
        locRepository.add(l21);
        spectacol.addToLocuri(l21);

        Loc l22 = new Loc(2, -1, 2, spectacol);
        locRepository.add(l22);
        spectacol.addToLocuri(l22);


        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(var receiver : loggedClients) {
            executor.execute(() -> {
                try {
                    receiver.updateSpectacole();
                }
                catch (TeatruException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }
//        for (Integer key : loggedClients.keySet()) {
//            IServiceObserver receiver = loggedClients.get(key);
//            executor.execute(() -> {
//                try {
//                    receiver.updateSpectacole();
//                }
//                catch (TeatruException e) {
//                    System.out.println("ERROR NOTIFYING ADMINS");
//                }
//            });
//        }
    }

    @Override
    public synchronized void logOut(Manager manager, IServiceObserver client) throws TeatruException {
        //IServiceObserver localClient = loggedClients.remove(manager.getId());
        loggedClients.remove(client);
//        if (localClient == null) {
//            throw new TeatruException("User is not logged in!");
//        }
    }

    @Override
    public void deleteSpectacol(Integer id, IServiceObserver client) throws TeatruException {
        Spectacol spectacol = spectacolRepository.findById(id);
        spectacolRepository.delete(spectacol);

        var locuri = locRepository.findBySpectacol(spectacol);
        for (var loc : locuri) {
            locRepository.delete(loc);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(var receiver : loggedClients) {
            executor.execute(() -> {
                try {
                    receiver.updateSpectacole();
                }
                catch (TeatruException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }
    }

    @Override
    public void updateSpectacol(SpectacolUpdateDTO spectacolDTO, IServiceObserver client) throws TeatruException {
        int idDate = 0;
        DateTime date = null;
        boolean done = false;
        while (!done) {
            idDate = ThreadLocalRandom.current().nextInt(0, 500000 + 1);
            date = new DateTime(spectacolDTO.getYear(), spectacolDTO.getMonth(), spectacolDTO.getDay(),
                    spectacolDTO.getHour(), spectacolDTO.getMinute());
            date.setId(idDate);
            try {
                dateTimeRepository.add(date);
                done = true;
            } catch (Exception ex) {

            }
        }

        Spectacol spectacol = null;
        done = false;
        while (!done) {
            spectacol = new Spectacol(spectacolDTO.getTitlu(), date, spectacolDTO.getRegizor(),
                    spectacolDTO.getGen(), spectacolDTO.getManager());
            spectacol.setId(spectacolDTO.getId());
            spectacol.setTeatru(spectacolDTO.getTeatru());
            try {
                spectacolRepository.update(spectacol, spectacolDTO.getId());
                done = true;
            } catch (Exception ex) {

            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(var receiver : loggedClients) {
            executor.execute(() -> {
                try {
                    receiver.updateSpectacole();
                }
                catch (TeatruException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }
    }

    @Override
    public List<Loc> getLocuriSpectacol(Spectacol spectacol, IServiceObserver client) throws TeatruException {
        return locRepository.findBySpectacol(spectacol);
    }

    @Override
    public Integer addRezervare(RezervareDTO rezervareDTO, IServiceObserver client) throws TeatruException {
        Spectator spectator = new Spectator(rezervareDTO.getNume(), rezervareDTO.getPrenume(), rezervareDTO.getEmail());
        Integer code = ThreadLocalRandom.current().nextInt(0, 500000 + 1);
        spectatorRepository.add(spectator);
        Rezervare rezervare = new Rezervare(code, spectator, rezervareDTO.getSpectacol());
        rezervareRepository.add(rezervare);
        for (var loc : rezervareDTO.getLocuri()) {
            loc.setRezervare(rezervare);
        }
        for (var loc : rezervareDTO.getLocuri()) {
            locRepository.update(loc, loc.getId());
        }
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(var receiver : loggedClients) {
            executor.execute(() -> {
                try {
                    receiver.updateLocuri();
                }
                catch (TeatruException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }
        return rezervare.getId();
    }

    @Override
    public void deleteRezervare(Integer cod, IServiceObserver client) throws TeatruException {
        Rezervare rezervare = rezervareRepository.findById(cod);
        List<Loc> locuri = locRepository.findAll();
        for (var loc : locuri) {
            if (loc.getRezervare() != null &&  loc.getRezervare().getId().equals(cod)) {
                loc.setRezervare(null);
                loc.setSpectacol(rezervare.getSpectacol());
                locRepository.update(loc, loc.getId());
            }
        }
        rezervareRepository.delete(rezervare);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(var receiver : loggedClients) {
            executor.execute(() -> {
                try {
                    receiver.updateLocuri();
                }
                catch (TeatruException e) {
                    System.out.println("ERROR NOTIFYING ADMINS");
                }
            });
        }
    }
}

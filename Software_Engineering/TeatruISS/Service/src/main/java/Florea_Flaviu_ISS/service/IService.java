package Florea_Flaviu_ISS.service;

import Florea_Flaviu_ISS.domain.*;

import java.util.List;

public interface IService {

    public Manager logIn(ManagerLogInDTO managerDTO, IServiceObserver client) throws TeatruException;

    public List<Spectacol> filtruDupaGen(Genuri gen, IServiceObserver client) throws TeatruException;

    public List<Spectacol> findAllSpectacole(IServiceObserver client) throws TeatruException;

    public void addSpectacol(SpectacolDTO spectacolDTO, IServiceObserver client) throws TeatruException;

    public void logOut(Manager manager, IServiceObserver client) throws TeatruException;

    public void deleteSpectacol(Integer id, IServiceObserver client) throws  TeatruException;

    public void updateSpectacol(SpectacolUpdateDTO spectacolDTO, IServiceObserver client) throws  TeatruException;

    public List<Loc> getLocuriSpectacol(Spectacol spectacol, IServiceObserver client) throws TeatruException;

    public Integer addRezervare(RezervareDTO rezervareDTO, IServiceObserver client) throws TeatruException;

    public void deleteRezervare(Integer cod, IServiceObserver client) throws TeatruException;
}

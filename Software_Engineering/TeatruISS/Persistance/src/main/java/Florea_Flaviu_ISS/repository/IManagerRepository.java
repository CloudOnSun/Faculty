package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.Manager;

public interface IManagerRepository extends Repository<Manager, Integer>{

    public Manager logIn(String email, String password);
}

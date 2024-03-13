package Florea_Flaviu_ISS.service;

import Florea_Flaviu_ISS.domain.Genuri;
import Florea_Flaviu_ISS.domain.Manager;
import Florea_Flaviu_ISS.domain.Spectacol;

import java.util.List;

public interface IServiceObserver {

    public void updateLocuri() throws TeatruException;

    public void updateSpectacole() throws TeatruException;

}

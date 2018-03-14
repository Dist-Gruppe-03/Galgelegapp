package com.quaade94.galgeleg;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface GalgeI extends java.rmi.Remote {

    @WebMethod public ArrayList<String> getBrugteBogstaver() throws java.rmi.RemoteException;

    @WebMethod public String getSynligtOrd() throws java.rmi.RemoteException;

    @WebMethod public String getOrdet() throws java.rmi.RemoteException;

    @WebMethod public int getAntalForkerteBogstaver() throws java.rmi.RemoteException;

    @WebMethod public boolean erSidsteBogstavKorrekt() throws java.rmi.RemoteException;

    @WebMethod public boolean erSpilletVundet() throws java.rmi.RemoteException;

    @WebMethod public boolean erSpilletTabt() throws java.rmi.RemoteException;

    @WebMethod public boolean erSpilletSlut() throws java.rmi.RemoteException;

    @WebMethod public void nulstil() throws java.rmi.RemoteException;

    @WebMethod public void opdaterSynligtOrd() throws java.rmi.RemoteException;

    @WebMethod public void gætBogstav(String bogstav) throws java.rmi.RemoteException;

    @WebMethod public void logStatus() throws java.rmi.RemoteException;
    
    @WebMethod boolean hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

    //public static String hentUrl(String url) throws java.rmi.RemoteException;
    @WebMethod public void hentOrdFraDr() throws java.rmi.RemoteException;
}
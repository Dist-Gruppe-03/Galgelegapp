package com.quaade94.galgeleg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import javax.jws.WebService;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(endpointInterface = "galgeleg.GalgeI")
public class Galgelogik  extends UnicastRemoteObject implements GalgeI {
  ArrayList<String> muligeOrd = new ArrayList<String>();
  private String ordet;
  private ArrayList<String> brugteBogstaver = new ArrayList<String>();
  private String synligtOrd;
  private int antalForkerteBogstaver;
  private boolean sidsteBogstavVarKorrekt;
  private boolean spilletErVundet;
  private boolean spilletErTabt;
  private Brugeradmin BA;
  private int scoren;
  private int[] Highscore = new int[10];
  private int[] revHighscore = new int[10];
  private int[] sortedHighscore = new int[10];
  

  public ArrayList<String> getBrugteBogstaver() {
    return brugteBogstaver;
  }


  public String getSynligtOrd() {
    return synligtOrd;
  }

  public String getOrdet() {
    return ordet;
  }

   public int getScoren() {
    return scoren;
  }
   
  public int getAntalForkerteBogstaver() {
    return antalForkerteBogstaver;
  }

  public boolean erSidsteBogstavKorrekt() {
    return sidsteBogstavVarKorrekt;
  }

  public boolean erSpilletVundet() {
    return spilletErVundet;
  }

  public boolean erSpilletTabt() {
    return spilletErTabt;
  }

  public boolean erSpilletSlut() {
    return spilletErTabt || spilletErVundet;
  }


  public Galgelogik() throws java.rmi.RemoteException {
    muligeOrd.add("bil");
    muligeOrd.add("computer");
    muligeOrd.add("programmering");
    muligeOrd.add("motorvej");
    muligeOrd.add("busrute");
    muligeOrd.add("gangsti");
    muligeOrd.add("skovsnegl");
    muligeOrd.add("solsort");
    muligeOrd.add("seksten");
    muligeOrd.add("sytten");
    nulstil();
  }

  public void nulstil() {
    brugteBogstaver.clear();
    antalForkerteBogstaver = 0;
    spilletErVundet = false;
    spilletErTabt = false;
    scoren = 0;
    ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
    opdaterSynligtOrd();
  }


  public void opdaterSynligtOrd() {
    synligtOrd = "";
    spilletErVundet = true;
    for (int n = 0; n < ordet.length(); n++) {
      String bogstav = ordet.substring(n, n + 1);
      if (brugteBogstaver.contains(bogstav)) {
        synligtOrd = synligtOrd + bogstav;
      } else {
        synligtOrd = synligtOrd + "*";
        spilletErVundet = false;
      }
    }
  }

  public void gætBogstav(String bogstav) {
      
    if (bogstav.length() != 1) return;
    System.out.println("Der gættes på bogstavet: " + bogstav);
    if (brugteBogstaver.contains(bogstav)) return;
    if (spilletErVundet || spilletErTabt) return;

    brugteBogstaver.add(bogstav);

    if (ordet.contains(bogstav)) {
      sidsteBogstavVarKorrekt = true;
      System.out.println("Bogstavet var korrekt: " + bogstav);
      scoren++;
    } else {
      // Vi gættede på et bogstav der ikke var i ordet.
      sidsteBogstavVarKorrekt = false;
      System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
      antalForkerteBogstaver = antalForkerteBogstaver + 1;
      if (antalForkerteBogstaver > 6) {
          skrivHighScore();
        spilletErTabt = true;
      }
      scoren--;
    }
    opdaterSynligtOrd();
  }

  public void logStatus() {
    System.out.println("---------- ");
    System.out.println("- ordet (skult) = " + ordet);
    System.out.println("- scoren = " + scoren);
    System.out.println("- synligtOrd = " + synligtOrd);
    System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
    System.out.println("- brugeBogstaver = " + brugteBogstaver);
    if (spilletErTabt){
        skrivHighScore();
        System.out.println("- SPILLET ER TABT");
    }
    if (spilletErVundet){
        skrivHighScore();
        System.out.println("- SPILLET ER VUNDET");
    }
    System.out.println("---------- ");
  }

  public boolean hentBruger(String brugernavn, String adgangskode) {
	  try {
	      BA = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
	      Bruger b = BA.hentBruger(brugernavn, adgangskode);
	      return true;
	  }
	  catch (Exception e) {
	      e.printStackTrace();
	  }
	
	return false;
  }
  
 
  public void skrivHighScore(){
      
      laesHighScore();
      List<String> lines = Arrays.asList(Integer.toString(Highscore[0]),Integer.toString(Highscore[1]),Integer.toString(Highscore[2]),Integer.toString(Highscore[3]),Integer.toString(Highscore[4]),Integer.toString(Highscore[5]),Integer.toString(Highscore[6]),Integer.toString(Highscore[7]),Integer.toString(Highscore[8]),Integer.toString(Highscore[9]));
        Path file = Paths.get("highscore.txt");
        
          try {
              Files.write(file, lines, Charset.forName("UTF-8"));
          } catch (IOException ex) {
              Logger.getLogger(Galgelogik.class.getName()).log(Level.SEVERE, null, ex);
          }
  }
  
   public int[] laesHighScore() {
       
      try {
          
          int counter = 0;
          BufferedReader in = new BufferedReader(new FileReader("highscore.txt"));
          String line;
          while((line = in.readLine()) != null){
              Highscore[counter] = Integer.parseInt(line);
              counter++;   
          }
          
          in.close();

          Arrays.sort(Highscore);
          Highscore[0] = scoren;
          Arrays.sort(Highscore);
               
          return Highscore;
      } catch (IOException ex) {
          List<String> lines = Arrays.asList("0");
          Path file = Paths.get("highscore.txt");
          try {
              Files.write(file, lines, Charset.forName("UTF-8"));
          } catch (IOException ex1) {
              Logger.getLogger(Galgelogik.class.getName()).log(Level.SEVERE, null, ex1);
          }
      }
  return Highscore;
   }

  public static String hentUrl(String url) throws IOException {
    System.out.println("Henter data fra " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    StringBuilder sb = new StringBuilder();
    String linje = br.readLine();
    while (linje != null) {
      sb.append(linje + "\n");
      linje = br.readLine();
    }
    return sb.toString();
  }

  public void hentOrdFraDr() throws RuntimeException {
    String data = null;
      try {
          data = hentUrl("https://dr.dk");
          //System.out.println("data = " + data);
      } catch (IOException ex) {
          Logger.getLogger(Galgelogik.class.getName()).log(Level.SEVERE, null, ex);
      }

    data = data.substring(data.indexOf("<body")). // fjern headere
            replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
            replaceAll("&#198;", "æ"). // erstat HTML-tegn
            replaceAll("&#230;", "æ"). // erstat HTML-tegn
            replaceAll("&#216;", "ø"). // erstat HTML-tegn
            replaceAll("&#248;", "ø"). // erstat HTML-tegn
            replaceAll("&oslash;", "ø"). // erstat HTML-tegn
            replaceAll("&#229;", "å"). // erstat HTML-tegn
            replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
            replaceAll(" [a-zæøå] "," "). // fjern 1-bogstavsord
            replaceAll(" [a-zæøå][a-zæøå] "," "); // fjern 2-bogstavsord

    //System.out.println("data = " + data);
    //System.out.println("data = " + Arrays.asList(data.split("\\s+")));
    muligeOrd.clear();
    muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

    System.out.println("muligeOrd = " + muligeOrd);
    nulstil();
  }
}
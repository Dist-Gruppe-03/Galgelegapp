package com.quaade94.galgeleg;

import java.rmi.Naming;

import javax.xml.ws.Endpoint;

/**
 *
 * @author krede
 */
public class Galgeserver {

    public static void main(String[] arg) throws Exception {
        java.rmi.registry.LocateRegistry.createRegistry(9923); // start i server-JVM

        GalgeI g = new Galgelogik();
        Naming.rebind("rmi://[::]:9923/galgeleg", g);
        Endpoint.publish("http://[::]:9924/galgeleg", g);
        System.out.println("Galgeleg startet.");

    }
}
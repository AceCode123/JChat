package me.mrtoucan.Tests;

import java.net.ServerSocket;

/**
 * Created by Anthony on 10/1/2016.
 */
public class JChat {

    public static void main(String[] args) {
        try {
            ServerSocket serversocket = new ServerSocket(8080);
            Utils u = new Utils(serversocket, 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}

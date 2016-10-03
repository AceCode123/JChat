package me.mrtoucan.Tests;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Anthony on 10/2/2016.
 */
public class Client {

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 8080);
            System.out.println(s.isConnected());
            Utils u = new Utils(s, 1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

package me.mrtoucan.Tests;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Anthony on 10/2/2016.
 */
public class Utils {

    private Socket s;
    private BufferedReader in;
    private BufferedWriter o;
    private String id;

    public Utils(Socket s, int id) {
        this.s = s;
        this.id = "Client";
        if(id == 0)
            this.id = "Server";
        if(s.isConnected())
            System.out.println(this.id + ": Connection Established!");
        try {
            o = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            ChatHandlers chat = new ChatHandlers(s, this.id, o);
            chat.start();
            FeedHandlers feed = new FeedHandlers(this.id, in);
            feed.start();
        } catch(Exception e) {}
    }

    public Utils(ServerSocket server, int id) {
        this.id = "Client";
        if(id == 0)
            this.id = "Server";
        System.out.println(this.id + ": Anticipating Client Connection!");
        Socket s = null;
        try {
            s = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.id + ": Connection Established With Client!");
        this.s = s;
        try {
            o = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            ChatHandlers chat = new ChatHandlers(s, this.id, o);
            chat.start();
            FeedHandlers feed = new FeedHandlers(this.id, in);
            feed.start();
        } catch(Exception e) {}
    }

    class ChatHandlers extends Thread {

        private Socket s;
        private String id;
        private Scanner sc = new Scanner(System.in);
        private BufferedWriter out;

        public ChatHandlers(Socket s, String id, BufferedWriter out) {
            this.s = s;
            this.id = id;
            this.out = out;
            System.out.println(id + ": Chat Handlers Activated!");
        }

        @Override
        public void run() {
            while(true) {
                try {
                    String fromServer;
                    while (sc.hasNextLine()) {
                        String message = this.id + ": " + sc.nextLine();
                        this.out.write(message);
                        this.out.newLine();
                        this.out.flush();
                        System.out.println(message);
                    }
                } catch(Exception e) {
                    this.stop();
                    closeConnection(this.s);
                    e.printStackTrace();
                }
            }
        }
    }

    class FeedHandlers extends Thread {
        private Scanner sc = new Scanner(System.in);
        private BufferedReader in;

        public FeedHandlers(String id, BufferedReader in) {
            this.in = in;
            System.out.println(id + ": Feed Handlers Activated!");
        }

        public void run() {
            while(true) {
                try {
                    String fromServer;
                    while ((fromServer = this.in.readLine()) != null) {
                        System.out.println(fromServer);
                    }
                } catch(Exception e) {
                    this.stop();
                    e.printStackTrace();
                }
            }
        }

    }

//    public void handleInputFeed() {
//        try {
//            String fromServer;
//            while ((fromServer = this.in.readLine()) != null) {
//                System.out.println(fromServer);
//            }
//        } catch(Exception e) {
//            closeConnection(this.s);
//            e.printStackTrace();
//        }
//
//    }
//
//    public void handleChat() {
//        try {
//            System.out.println(id + ": Chat Handler Activated!");
//            Scanner sc = new Scanner(System.in);
//            while (sc.hasNextLine()) {
//                String message = this.id + ": " + sc.nextLine();
//                this.o.write(message);
//                this.o.newLine();
//                this.o.flush();
//                System.out.println(message);
//            }
//        } catch(Exception e) {
//            System.out.println("Failed to send message! Please try again later!");
//        }
//    }

    public static void closeConnection(Socket s) {
        try {
            s.close();
        } catch(Exception e) {}
    }

}

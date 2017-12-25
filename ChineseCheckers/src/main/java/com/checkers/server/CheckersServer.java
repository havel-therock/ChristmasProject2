package com.checkers.server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


class CheckersServer {

    // serverWindow;
    ServerSocket mainSocket;
    ArrayList<Runnable> playerList = new ArrayList<Runnable>();
    ArrayList<Game> gameList = new ArrayList<Game>();

    public static void main(String[] args){
        CheckersServer a = new CheckersServer();
        //a.startGui();
        a.startServer();
    }
 /*
    void startGui(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverWindow = new GuiServer();
                serverWindow.setVisible(true);
            }
        });
    }
    */

    void startServer() {
        try {
            mainSocket = new ServerSocket(4444);
            System.out.println("Server working...");
            catchNewClient();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Server failed while booting");
        }
    }

    void catchNewClient() {
        try {
            while (true) {
                Socket communicationSocket = mainSocket.accept();
                System.out.println("new player connected");
                createNewPlayer(communicationSocket);
                System.out.println("new player created");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server failed while catching new client");

        }
    }

    void createNewPlayer(Socket communicationSocket){
        try {
            InputStreamReader reader_stream = new InputStreamReader(communicationSocket.getInputStream());
            BufferedReader reader = new BufferedReader(reader_stream);
            PrintWriter writer = new PrintWriter(communicationSocket.getOutputStream(), true);
            Runnable player = new Player(reader,writer,gameList);
            playerList.add(player);

            Thread playerManagement = new Thread(player);
            playerManagement.start();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Server failed while creating player");
        }

    }


}
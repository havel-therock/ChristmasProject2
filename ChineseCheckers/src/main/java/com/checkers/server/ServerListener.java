package com.checkers.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerListener implements Runnable{

    ArrayList<Player> playerList;
    ArrayList<Game> gameList;
    ServerSocket mainSocket;

    ServerListener(ArrayList<Player> playerList, ArrayList<Game> gameList){
        this.gameList = gameList;
        this.playerList = playerList;
    }

    public void run(){
        startServer();
        System.out.println("ServerListener has closed");
    }

    void startServer() {
        try {
            mainSocket = new ServerSocket(4444);
            System.out.println("Server working...");
            catchNewClient();

        } catch ( BindException e){
            System.out.println("Cannot create two servers on the sam socket");
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Server failed while booting");
        }
    }

    void catchNewClient() {
        try {
            while (true) {
                Socket communicationSocket = mainSocket.accept();
                createNewPlayer(communicationSocket);
                System.out.println("new player created");
            }
        }catch (SocketException ex){
            System.out.println("Server closing...");
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
            Player player = new Player(reader,writer,gameList);
            playerList.add(player);

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Server failed while creating player");
        }
    }
}

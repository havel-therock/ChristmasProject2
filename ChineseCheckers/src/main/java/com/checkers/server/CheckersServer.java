package com.checkers.server;



import javax.swing.*;
import java.util.ArrayList;


class CheckersServer {

    ServerListener serverListener;
    Thread listenerThread;
    GuiServer serverGui;

    ArrayList<Player> playerList = new ArrayList<>();
    ArrayList<Game> gameList = new ArrayList<Game>();

    public static void main(String[] args){
        CheckersServer a = new CheckersServer();
        a.createGui();
    }

    void createGui(){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverGui = new GuiServer(CheckersServer.this);
                serverGui.setVisible(true);
            }
        });

    }

    void createListener(){

        serverListener = new ServerListener(playerList,gameList);
        listenerThread = new Thread (serverListener);
        listenerThread.start();

    }


}

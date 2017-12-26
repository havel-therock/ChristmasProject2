package com.checkers.server;

import java.io.IOException;

public class PlayerListener implements Runnable {

    Player player;
    String userLine;

    PlayerListener(Player player){
        this.player = player;
    }

    public void run(){
        while(player.connected) {
            readFromPlayer();
        }
    }

    void readFromPlayer(){
        try{
            userLine = null;
            while(userLine == null) {
                userLine = player.reader.readLine();
                System.out.println(userLine);
                player.parseLine(userLine);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

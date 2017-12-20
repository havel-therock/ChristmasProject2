package com.checkers.server;

import com.checkers.client.CheckersClient;

import java.util.ArrayList;

public class Game {
    Board b;
    Rules r;
    String name;
    ArrayList<Runnable> playerList = new ArrayList<Runnable>();

    Game(String[] arguments, Player player){
        this.name = arguments[1];
        b = new Board(arguments);
        r = new Rules();
        playerList.add(player);

    }

    public String getName() {
        return name;
    }

    protected void addPlayer(Player player){
        playerList.add(player);
    }

    protected void delete(Player player){
        playerList.remove(player);
    }

    protected void deletePieces(int number){
        b.deletePlayer(number);
    }
    protected void sendMessage(String message){
        for(int i=0;i<playerList.size();i++){
            Player currentPlayer = (Player) playerList.get(i);
            currentPlayer.writeToPlayer(message);
        }
    }

}

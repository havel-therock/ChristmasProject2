package com.checkers.server;

import com.checkers.server.board.*;
import java.util.ArrayList;

public class Game {
    Board b;
    Rules r;
    String name;
    ArrayList<Player> playerList = new ArrayList<>();

    Game(String[] arguments, Player player) throws WrongData {
        this.name = arguments[1];
        playerList.add(player);
        b = new Board(arguments);
        r = new Rules();


    }

    public String getName() {
        return name;
    }

    protected boolean addPlayer(Player player){
        if(playerList.size()<b.getPlayers()) {
            playerList.add(player);
            return true;
        }else{
            return false;
        }
    }

    protected void delete(Player player){
        playerList.remove(player);
    }

    protected void deletePieces(int number){
        b.deletePlayer(number);
    }
    protected void sendMessage(String message){
        for(int i=0;i<playerList.size();i++){
            playerList.get(i).writeToPlayer(message);
        }
    }
    protected int getCurrentPlayersNumber(){
        return playerList.size();
    }
    protected Board getBoard(){
        return b;
    }

}

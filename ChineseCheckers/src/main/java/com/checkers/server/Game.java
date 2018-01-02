package com.checkers.server;

import com.checkers.server.board.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    Board b;
    Rules r;
    String name;
    int ready;
    boolean isStarted;
    ArrayList<Player> playerList = new ArrayList<>();

    Game(String[] arguments, Player player) throws WrongData {
        this.name = arguments[1];
        playerList.add(player);
        b = new Board(arguments);
        r = new Rules();
        ready = 0;
        isStarted = false;


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

   synchronized protected int getCurrentPlayersNumber(){
        return playerList.size();
    }

    protected Board getBoard(){
        return b;
    }

    protected void setReady(boolean state){
        if(!isStarted) {
            if (state) {
                ready++;
            } else {
                ready--;
            }
            if (ready == b.getPlayers()) {
                isStarted = true;
                random();
            }
        }
    }

    private void random(){
        Random r = new Random();
        int i;
        i = r.nextInt(b.getPlayers());
        setActivePlayer(i);
    }

    protected void setActivePlayer(int i){
        if(getCurrentPlayersNumber()!=0) {
            playerList.get(i).setIfActive(true);
        }
    }
    protected boolean getIsStarted(){
        return isStarted;
    }
    protected void setPlayersNumbers(){
        for (int i =0;i<playerList.size();i++) {
            playerList.get(i).setNumber(i);
        }
    }
}

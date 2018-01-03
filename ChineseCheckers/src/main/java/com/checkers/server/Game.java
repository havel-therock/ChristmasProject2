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
    boolean [] freeCorners;
    ArrayList<Player> playerList = new ArrayList<>();

    Game(String[] arguments, Player player) throws WrongData {
        this.name = arguments[1];
        b = new Board(arguments);
        r = new Rules.Builder(b.graph,b.fieldsPerRow,b.tempBoard)
                .moveOnlyToEmptyField(true)
                .canNotEscapeTargetCorner(true)
                .moveOnlyYourPieces(true)
                //.stepsizeOneOnly(true)
                .jumpOverOneOnly(true)
                //.multiJumpsOver(true)
                .build();
        //create unique rules
        freeCorners = new boolean [b.getPlayers()];
        for(int i = 0; i<b.getPlayers(); i++){
            freeCorners [i] = true;
        }

        isStarted = false;

        addPlayer(player);
    }

    public String getName() {
        return name;
    }

    protected boolean addPlayer(Player player){
        if(playerList.size()<b.getPlayers()) {
            playerList.add(getNextIndex(),player);
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

    protected void setReady(int index){
        if(!isStarted) {
          freeCorners[index] = false;

            if (getNextIndex() == -1) {
                sendMessage("Game started");
                isStarted = true;
                random();
            }
        }
    }

    protected void setNotReady (int index){
        if(!isStarted){
            freeCorners[index] = true;
        }
    }
    private void random(){
        Random r = new Random();
        int i;
        i = r.nextInt(b.getPlayers());
        setActivePlayer(i);
    }

    protected void setActivePlayer(int i){
        if(getCurrentPlayersNumber()>0) {
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

    protected boolean isWon(){

        int i = r.checkIfWon();
        Player tmp = null;
        if(i>0&&i<7){
            for(Player current : playerList){
                if(current.getCornerNumber() == i) {
                    current.writeToPlayer("Congratulations, you won!");
                    tmp = current;
                }else{
                    current.writeToPlayer("won;"+i);
                }
            }
            b.deletePlayer(tmp.getCornerNumber());
            playerList.remove(tmp);

            for(Player current : playerList){
                current.writeToPlayer("boardReset;" + b.getBoard());
            }

            if(playerList.size()==1){
                sendMessage("gameover");
            }
            return true;
        }else{
            return false;
        }

    }
    protected int getNextIndex(){
        for(int i = 0; i<freeCorners.length; i++){
            if(freeCorners[i]){
                return i;
            }
        }
        return -1;
    }
}

package com.checkers.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Player implements Runnable{
    ArrayList<Game> gameList;
    boolean connected;
    boolean ifActive;
    int number; // number of your team (pieces)
    BufferedReader reader;
    PrintWriter writer;
    String idGame;

    Player(BufferedReader reader, PrintWriter writer, ArrayList<Game> gameList){
        idGame = null;
        this.gameList = gameList;
        ifActive = false;
        number = 0;
        this.reader = reader;
        this.writer = writer;
        connected=true;
    }

    public void run(){

        while(connected) {
                readFromPlayer();
                writeToPlayer("Pobrano dane z bufora");
        }
    }

    void parseLine(String line){

        String[] arguments = line.split(";");
        switch (arguments[0]){
            case "newg":
                newCmd(arguments);
                break;

            case "join":
                joinCmd(arguments);
                break;

            case "exit":
                exitCmd(arguments);
                break;

            case "move":
                moveCmd(arguments);
                break;

            default:
                break;
        }
    }

    void writeToPlayer(String message){
        writer.println(message);
        writer.flush();
    }

    void readFromPlayer(){
        try{
            String userLine = null;
            while(userLine == null) {
                userLine = reader.readLine();
                System.out.println(userLine);
                parseLine(userLine);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void newCmd(String[] arguments){
        boolean exist = false;
        int i;
        for(i = 0; i < gameList.size(); i++){
            if(gameList.get(i).getName() == arguments[1]){
                exist = true;
                break;
            }
        }
        if(exist){
            writeToPlayer("Game already exists (change name)");
        }else{
            Game game = new Game(arguments, this);
            gameList.add(game);
            this.idGame = game.getName();
            writeToPlayer("Successfully created a game");
        }
    }

    private void joinCmd(String[] arguments){
        boolean exist = false;
        int i;
        for(i = 0; i < gameList.size(); i++){
            if(gameList.get(i).getName() == arguments[1]){
                exist = true;
                break;
            }
        }
        if(exist){
            gameList.get(i).addPlayer(this);
            this.idGame = gameList.get(i).getName();
            writeToPlayer("Successfully joined the game");
        }else{
            writeToPlayer("Game does not exist");
        }
    }

    private void moveCmd(String[] arguments) {
        int i;
        for (i = 0; i < gameList.size(); i++) {
            if (gameList.get(i).getName() == idGame) {
                if (gameList.get(i).r.checkMove(arguments[1])) {
                    gameList.get(i).b.executeMove(arguments[1]);
                }
            }
        }
    }

    private void exitCmd(String[] arguments){
        int i;
        this.connected=false;
        writer.println("Player removed");
        writer.flush();
        for(i = 0; i < gameList.size(); i++){
            if(gameList.get(i).getName() == idGame){
                gameList.get(i).deletePieces(number);
                gameList.get(i).delete(this);
                //sth to stop the thread
            }
        }
    }

    protected void setIfActive(boolean state){
        this.ifActive = state;
    }

    protected boolean getIfActive(){
        return this.ifActive;
    }

    protected void setNumber(int number){
        this.number = number;
    }

    protected int getNumber(){
        return this.number;
    }

}

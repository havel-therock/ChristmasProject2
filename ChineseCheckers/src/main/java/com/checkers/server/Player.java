package com.checkers.server;

import com.checkers.server.board.WrongData;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Player {
    ArrayList<Game> gameList;
    volatile boolean connected;
    boolean ifActive;
    int number;
    BufferedReader reader;
    PrintWriter writer;
    String idGame;
    String name;
    PlayerListener playerListener;
    Thread listenerThread;

    Player(BufferedReader reader, PrintWriter writer, ArrayList<Game> gameList){
        idGame = null;
        ifActive = false;
        connected=true;
        number = 0;

        this.name = "Anonymous";
        this.gameList = gameList;
        this.reader = reader;
        this.writer = writer;

        playerListener = new PlayerListener(this);
        listenerThread = new Thread(playerListener);
        listenerThread.start();
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
                exitCmd();
                break;

            case "move":
                moveCmd(arguments);
                break;
            case "name":
                nameCmd(arguments[1]);
                break;
            case "refresh":
                refreshCmd();
                break;

            default:
                break;
        }
    }

    protected void writeToPlayer(String message){
        writer.println(message);
        writer.flush();
        System.out.println(message);
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
            writeToPlayer("Game already exists");
        }else{
            try {
                Game game = new Game(arguments, this);
                gameList.add(game);
                this.idGame = game.getName();
                writeToPlayer("Successfully created a game");
                writeToPlayer(game.b.getBoard());
            }catch (WrongData ex){
                writeToPlayer(ex.message);
            }

        }
    }

    private void joinCmd(String[] arguments){
        boolean exist = false;
        int i;
        for(i = 0; i < gameList.size(); i++){
            if(gameList.get(i).getName().equals(arguments[1])){
                exist = true;
                break;
            }
        }
        if(exist){
            gameList.get(i).addPlayer(this);
            this.idGame = gameList.get(i).getName();
            writeToPlayer("Successfully joined the game");
            writeToPlayer(gameList.get(i).b.getBoard());
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
                    gameList.get(i).sendMessage(arguments[1]);
                }
            }
        }
    }

    protected void exitCmd(){
        int i;
        this.connected=false;
        writeToPlayer("Player removed");
        for(i = 0; i < gameList.size(); i++){
            if(gameList.get(i).getName() == idGame){
                gameList.get(i).deletePieces(number);
                gameList.get(i).delete(this);
            }
        }
    }

    void nameCmd(String name){
        if(name.equals("Anonymous")){
            writeToPlayer("Name valid");
            return;
        }
        for (Game currentG : gameList) {
            for(Player currentP : currentG.playerList){
                if(currentP.name.equals(name)){
                    writeToPlayer("Name invalid");
                    return;
                }
            }
        }
        writeToPlayer("Name valid");
        this.name = name;
    }

    void refreshCmd(){
        String tmp;
        tmp = "refreshed";
        for (Game current: gameList) {
            tmp=tmp+";"+current.getName();
        }
        writeToPlayer(tmp);
    }
}

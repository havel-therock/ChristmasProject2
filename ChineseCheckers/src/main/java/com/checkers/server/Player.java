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
    int cornerNumber;
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

        this.name = "anonymous";
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
                moveCmd(line);
                break;

            case "name":
                nameCmd(arguments[1]);
                break;

            case "refresh":
                refreshCmd();
                break;

            case "delete":
                deleteCmd(arguments[1]);
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
                this.number = 0;
                this.cornerNumber = game.b.getWhichCorners(this.number);
                writeToPlayer("Successfully created a game");
                writeToPlayer("boardSetUp;"+game.b.getBoard());
                writeToPlayer("info;"+Integer.toString(this.cornerNumber)+";"+this.name+";"+idGame);
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
            if(gameList.get(i).addPlayer(this)) {
                this.idGame = gameList.get(i).getName();
                this.number = gameList.get(i).getCurrentPlayersNumber()-1;
                this.cornerNumber = gameList.get(i).getBoard().getWhichCorners(this.number);
                writeToPlayer("Successfully joined the game");
                writeToPlayer("boardSetUp;"+gameList.get(i).b.getBoard());
                writeToPlayer("info;"+Integer.toString(this.cornerNumber)+";"+this.name+";"+idGame);
            }else{
                writeToPlayer("This game is full, please select a different game or create a new one");
            }
        }else{
            writeToPlayer("Game does not exist");
        }
    }

    private void moveCmd(String line) {      //parsuj to sobie w planszy i zasadach jak chesz xD
        int i;                                // line to ten string ktory dostaje od klienta w formacie "move;x;y;x;y"
        for (i = 0; i < gameList.size(); i++) {
            if (gameList.get(i).getName() == idGame) {
                if (gameList.get(i).r.checkMove(line)) {
                    gameList.get(i).b.executeMove(line);
                    gameList.get(i).sendMessage(line);
                }else {
                    writeToPlayer("This move is illegal :( ");
                }
            }
        }
    }

    protected void exitCmd(){
        int i;
        this.connected=false;
        writeToPlayer("Player removed");

        for(i = 0; i < gameList.size(); i++){

            if(gameList.get(i).getName().equals(idGame)){

                gameList.get(i).deletePieces(this.cornerNumber);
                gameList.get(i).delete(this);

                for (Player current: gameList.get(i).playerList) {
                    current.writeToPlayer("Jeden z graczy opuscil gre");
                    current.writeToPlayer("boardReset;"+gameList.get(i).b.getBoard());
                }
            }
        }

    }

    void nameCmd(String name){
        if(name.equals("anonymous")){
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
    void deleteCmd(String name){
        for (int i = 0; i < gameList.size(); i++) {
            if (gameList.get(i).getName().equals(name)) {
                if(gameList.get(i).getCurrentPlayersNumber()<1){
                    gameList.remove(i);
                    refreshCmd();
                }else{
                    writeToPlayer("This game is still in use");
                }
            }
        }
    }
}

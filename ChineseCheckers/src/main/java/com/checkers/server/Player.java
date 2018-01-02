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

            case"start":
                startCmd();

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
                gameList.get(i).setReady(true);
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
        if(exist) {
            if (!gameList.get(i).getIsStarted()) {
                if (gameList.get(i).addPlayer(this)) {
                    this.idGame = gameList.get(i).getName();
                    this.number = gameList.get(i).getCurrentPlayersNumber() - 1;
                    this.cornerNumber = gameList.get(i).getBoard().getWhichCorners(this.number);
                    writeToPlayer("Successfully joined the game");
                    writeToPlayer("boardSetUp;" + gameList.get(i).b.getBoard());
                    writeToPlayer("info;" + Integer.toString(this.cornerNumber) + ";" + this.name + ";" + idGame);
                    gameList.get(i).setReady(true);
                } else {
                    writeToPlayer("This game is full, please select a different game or create a new one");
                }
            } else {
                writeToPlayer("This game has already started, please select a different game or create a new one ");
            }
        }else{
            writeToPlayer("Game does not exist");
        }
    }

    private void moveCmd(String line) {
        int i;
        if(ifActive) {
            for (i = 0; i < gameList.size(); i++) {
                if (gameList.get(i).getName() == idGame) {
                    if (gameList.get(i).r.checkMove(line)) {
                        gameList.get(i).b.executeMove(line);
                        gameList.get(i).sendMessage(line);
                        if(isNextPlayer(i)) {
                            setNextPlayerActive(i);
                        }

                    } else {
                        writeToPlayer("This move is illegal :( ");
                    }
                }
            }
        }else{
            writeToPlayer("It's not your move yet :(");
        }
    }

    protected void exitCmd(){
        int i;
        this.connected=false;
        writeToPlayer("Player removed");

        for(i = 0; i < gameList.size(); i++){

            if(gameList.get(i).getName().equals(idGame)){

                gameList.get(i).delete(this);
                gameList.get(i).setReady(false);

                if(gameList.get(i).isStarted) {

                    gameList.get(i).deletePieces(this.cornerNumber);

                    for (Player current : gameList.get(i).playerList) {
                        current.writeToPlayer("Jeden z graczy opuscil gre");
                        current.writeToPlayer("boardReset;" + gameList.get(i).b.getBoard());
                    }

                    if(ifActive){
                        if(gameList.get(i).getCurrentPlayersNumber()==1){
                            gameList.get(i).sendMessage("Congratulations, you won!");
                        } else if(number ==  gameList.get(i).getCurrentPlayersNumber()){
                            gameList.get(i).setActivePlayer(0);
                        }else{
                            gameList.get(i).setActivePlayer(number);
                            gameList.get(i).setPlayersNumbers();
                        }
                    }else{
                        if(gameList.get(i).getCurrentPlayersNumber()==1){
                            gameList.get(i).sendMessage("Congratulations, you won!");
                        }else if(number ==  gameList.get(i).getCurrentPlayersNumber()){
                            return;
                        }else{
                            gameList.get(i).setPlayersNumbers();
                        }

                    }
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

    private void startCmd(){
        for (int i = 0; i < gameList.size(); i++) {
            if (gameList.get(i).getName().equals(name)) {
                gameList.get(i).setReady(true);
            }
        }
    }

    protected void setIfActive(boolean state){
        ifActive = state;
        writeToPlayer("It's your turn");

    }

     private void setNextPlayerActive(int i){
        if(number ==  gameList.get(i).getCurrentPlayersNumber()-1){
            gameList.get(i).setActivePlayer(0);
        }else{
            gameList.get(i).setActivePlayer(number+1);
        }
         ifActive = false;
     }

     private boolean isNextPlayer(int i){
        if(gameList.get(i).getCurrentPlayersNumber()==1){
            writeToPlayer("Congratulations, you won!");
            return false;
        }else{
            return true;
        }
     }
    protected void setNumber(int i){
        this.number = i;
    }
}

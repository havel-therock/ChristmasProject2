package com.checkers.server;

import com.checkers.server.board.WrongData;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Player {

    private ArrayList<Game> gameList;
    private Game myGame;
    protected volatile boolean connected;
    private boolean ifActive;
    private int number;
    private int cornerNumber;
    protected BufferedReader reader;
    private PrintWriter writer;
    private String idGame;
    private String name;
    private PlayerListener playerListener;
    protected Thread listenerThread;

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
            case "next":
                nextCmd();
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
                myGame = game;
                this.idGame = myGame.getName();
                this.number = 0;
                this.cornerNumber = myGame.b.getWhichCorners(this.number);
                writeToPlayer("Successfully created a game");
                writeToPlayer("boardSetUp;"+myGame.b.getBoard());
                writeToPlayer("info;"+Integer.toString(this.cornerNumber)+";"+this.name+";"+idGame);
                myGame.setReady(true);
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
                    myGame = gameList.get(i);
                    this.idGame = myGame.getName();
                    this.number = myGame.getCurrentPlayersNumber() - 1;
                    this.cornerNumber = myGame.getBoard().getWhichCorners(this.number);
                    writeToPlayer("Successfully joined the game");
                    writeToPlayer("boardSetUp;" + myGame.b.getBoard());
                    writeToPlayer("info;" + Integer.toString(this.cornerNumber) + ";" + this.name + ";" + idGame);
                    myGame.setReady(true);
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
        if(ifActive) {
            if (myGame.r.checkMove(line)) {
                System.out.println("+++ board before move;"+myGame.b.getBoard());
                myGame.b.executeMove(line);
                System.out.println("+++  board after move;"+myGame.b.getBoard());
                myGame.sendMessage(line);
                if(isNextPlayer()) {
                    setNextPlayerActive();
                }
            } else {
                writeToPlayer("This move is illegal :( ");
            }
        }else{
            writeToPlayer("It's not your move yet :(");
        }
    }

    protected void exitCmd(){

        this.connected=false;
        writeToPlayer("Player removed");

        myGame.delete(this);
        myGame.setReady(false);

        if(myGame.isStarted) {
            System.out.println("+++ board before deleting"+myGame.b.getBoard());
            myGame.deletePieces(this.cornerNumber);

            for (Player current : myGame.playerList) {
                current.writeToPlayer("One of the players has quit!");
                current.writeToPlayer("boardReset;" + myGame.b.getBoard());
            }
            System.out.println("--- thats the deletion");
            System.out.println("+++ board after deleting"+myGame.b.getBoard());
            if(ifActive){
                if(myGame.getCurrentPlayersNumber()==1){
                    myGame.sendMessage("Congratulations, you won!");
                } else if(number ==  myGame.getCurrentPlayersNumber()){
                       myGame.setActivePlayer(0);
                }else{
                    myGame.setActivePlayer(number);
                    myGame.setPlayersNumbers();
                }
            }else{
                if(myGame.getCurrentPlayersNumber()==1){
                    myGame.sendMessage("Congratulations, you won!");
                }else if(number ==  myGame.getCurrentPlayersNumber()){
                    return;
                }else{
                    myGame.setPlayersNumbers();
                }
            }
        }

    }

    private void nextCmd(){
        if(ifActive){
            setIfActive(false);
            setNextPlayerActive();
        }else{
            writeToPlayer("It's not your move yet :(");
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

    protected void setIfActive(boolean state){
        ifActive = state;
        if(state == true) {
            writeToPlayer("It's your turn");
        }
    }

     private void setNextPlayerActive(){
        if(number ==  myGame.getCurrentPlayersNumber()-1){
            myGame.setActivePlayer(0);
        }else{
            myGame.setActivePlayer(number+1);
        }
         ifActive = false;
     }

     private boolean isNextPlayer(){
        if(myGame.getCurrentPlayersNumber()==1){
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

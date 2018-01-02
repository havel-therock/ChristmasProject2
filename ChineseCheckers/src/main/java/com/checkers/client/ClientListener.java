package com.checkers.client;

import java.io.BufferedReader;
import java.io.IOException;

class ClientListener implements Runnable  {

    private CheckersClient checkersClient;
    private BufferedReader reader;
    private  GuiSetup setupWindow;
    protected  GuiWelcome welcomeWindow;
    protected  GuiGame gameWindow;
    private volatile boolean end;

    ClientListener(CheckersClient checkersClient, BufferedReader reader, GuiWelcome welcomeWindow){

      this.checkersClient = checkersClient;
      this.reader = reader;
      this.welcomeWindow = welcomeWindow;
      this.end = false;
    }

    public void setGameBoard(GuiGame gameWindow){
        this.gameWindow = gameWindow;
    }

    void setEnd(){
        this.end = true;
    }

    public void run() {
      while(!end){
          listen();
      }
    }

    protected void parseLine(String line){

        switch (line){
            case "Game already exists":
                setupWindow.showMessage(line);
                break;

            case "Successfully created a game":
                checkersClient.newGameCmd(line);
                break;

            case "Successfully joined the game":
                checkersClient.joinGameCmd(line);
                break;

            case "Game does not exist":
                welcomeWindow.showMessage(line);
                break;

            case "Server will stop soon":
                checkersClient.handleServerClosing();
                break;
            case "Player removed":
                break;
            case "Name valid" :
                welcomeWindow.setHasName(true);
                break;
            case "Name invalid":
                welcomeWindow.setHasName(false);
                break;
            case "This game is still in use":
                welcomeWindow.showMessage(line);
                break;
            case "Congratulations, you won!" :
                gameWindow.showMessage("Congratulations, you won!");
                gameWindow.endGame();
                break;
            case "One of the players has quit!" :
                gameWindow.writeMessage(line);
                break;

            default:
                if (line.matches ("(move).*")){
                    gameWindow.boardCmd(line);
                } else if(line.matches("(board).*")){
                    gameWindow.boardCmd(line);
                } else if(line.matches("(info).*")){
                    gameWindow.boardCmd(line);
                } else if (line.matches("(refreshed).*")) {
                    welcomeWindow.setList(line);
                } else {
                    checkersClient.showMessage(line);
                }
                break;
        }
    }


    protected void listen(){
            try{
                String serverLine = null;
                while(serverLine == null) {
                    serverLine = reader.readLine();
                    System.out.println(serverLine);
                    parseLine(serverLine);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
    }

    protected void setSetupWindow(GuiSetup setup){
        this.setupWindow = setup;
    }
    protected void setGameWindow(GuiGame game){
        this.gameWindow = game;
    }

}

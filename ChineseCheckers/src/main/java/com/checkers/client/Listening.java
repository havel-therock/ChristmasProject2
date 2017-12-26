package com.checkers.client;

import java.io.BufferedReader;
import java.io.IOException;

class Listening  implements Runnable  {

    private ClientListener clientListener;
    private BufferedReader reader;
    private  GuiSetup setupWindow;
    protected  GuiWelcome welcomeWindow;
    protected  GuiGame gameWindow;
    private volatile boolean end;

    Listening(ClientListener listener, BufferedReader reader, GuiWelcome welcomeWindow,
              GuiSetup setupWindow, GuiGame gameWindow){

      this.clientListener = listener;
      this.reader = reader;
      this.welcomeWindow = welcomeWindow;
      this.setupWindow = setupWindow;
      this. gameWindow = gameWindow;

      this.end = false;
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
                clientListener.newGameCmd(line);
                break;

            case "Successfully joined the game":
                clientListener.joinGameCmd(line);
                break;

            case "Game does not exist":
                welcomeWindow.showMessage(line);
                break;

            case "Server will stop soon":
                clientListener.handleServerClosing();
                break;
            case "Player removed":
                break;

            default:
                clientListener.showMessage(line);
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


}

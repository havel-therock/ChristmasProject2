package com.checkers.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CheckersClient{

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    GuiHandler handler;
    ClientListener listener;
    private GuiSetup setupWindow;
    private GuiWelcome welcomeWindow;
    private GuiGame gameWindow;

    public static void main(String[] args){
        CheckersClient client = new CheckersClient();
        client.createHandler();
        //Thread listen = new ClientListener(client.listener);

    }


    private void createHandler(){
        handler = new GuiHandler(this);

    }


    protected boolean connectServer(String hostName){
        try{
            socket = new Socket(hostName, 4444);
            InputStreamReader inStream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inStream);
            writer = new PrintWriter(socket.getOutputStream());
            return true;
        }catch(IOException e){
           // e.printStackTrace();
            System.out.println("Failed while connecting server");
            return false;
        }

    }


    public  void joinGame(String name){
        if(name.equals("")){
            welcomeWindow.showMessage("Game name cannot be empty");
            return;
        }else if(name.length()>10){
            welcomeWindow.showMessage("Game name is too long");
            return;
        }else {
            if(joinGameRequest(name)){
                handler.createBoardWindow();
                welcomeWindow.setVisible(false);
                welcomeWindow.dispose();
                if(setupWindow!=null){
                    setupWindow.setVisible(false);
                    setupWindow.dispose();
                }
            } else{
                welcomeWindow.showMessage("Cannot join game");
            }
        }
    }

    public void newGame(String data){               // dummy method
        if(newGameRequest(data)) {
            handler.createBoardWindow();
            if(setupWindow!=null){
                setupWindow.setVisible(false);
                setupWindow.dispose();
            }
            if(welcomeWindow!=null){
                welcomeWindow.setVisible(false);
                welcomeWindow.dispose();
            }
        }
    }

    private boolean newGameRequest(String data){
        writer.println("newg;" + data);
        writer.flush();
        return true;
    }

    private boolean joinGameRequest(String name){           //dummy method
        if(name.equals("bad"))
            return false;
        else
            return true;
    }

    void setGameWindow(GuiGame gameWindow){
        this.gameWindow=gameWindow;
    }
    void setWelcomeWindow(GuiWelcome welcomeWindow){
        this.welcomeWindow=welcomeWindow;
    }
    void setSetupWindow(GuiSetup setupWindow){
        this.setupWindow=setupWindow;
    }
    GuiHandler getGuiHandler(){
        return handler;
    }
}

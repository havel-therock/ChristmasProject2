package com.checkers.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class GuiHandler {

    private GuiSetup setupWindow;
    private GuiWelcome welcomeWindow;
    private GuiGame gameWindow;
    private CheckersClient client;

    GuiHandler(CheckersClient client){
        createGuiWelcome();
        this.client=client;
    }


    private void createGuiWelcome(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                welcomeWindow = new GuiWelcome(GuiHandler.this);
                welcomeWindow.setVisible(true);
            }
        });
    }

    protected void createGuiSetup(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setupWindow = new GuiSetup(GuiHandler.this);
                setupWindow.setVisible(true);
            }
        });
    }

    private void createBoardWindow(){                        //dummy method
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameWindow = new GuiGame();
                gameWindow.setVisible(true);
            }
        });
    }

    protected boolean tryConnection(String hostName){
        return client.connectServer(hostName);
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
                createBoardWindow();
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
            createBoardWindow();
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
            client.writer.println("newg;" + data);
            client.writer.flush();
            return true;
    }

    private boolean joinGameRequest(String name){           //dummy method
        if(name.equals("bad"))
            return false;
        else
            return true;
    }

}
package com.checkers.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class GuiHandler {

    private GuiSetup setupWindow;
    private GuiWelcome welcomeWindow;
    private GuiGame gameWindow;
    private CheckersClient client;
    protected int activeWindows;
    protected ClientListener listener;
    protected boolean isConnected;

    GuiHandler(CheckersClient client){
        createGuiWelcome();
        this.client=client;
        activeWindows=0;

    }


    private void createGuiWelcome(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                welcomeWindow = new GuiWelcome(client);
                welcomeWindow.setVisible(true);
                client.setWelcomeWindow(welcomeWindow);
                activeWindows++;
            }
        });
    }

    protected void createGuiSetup(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(activeWindows==1) {
                    setupWindow = new GuiSetup(listener);
                    setupWindow.setVisible(true);
                    listener.setSetupWindow(setupWindow);
                    activeWindows++;
                }
            }
        });
    }

    protected void createBoardWindow(){                        //dummy method
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameWindow = new GuiGame(listener);
                gameWindow.setVisible(true);
                listener.setGameWindow(gameWindow);
                activeWindows++;
                closeWindows();
            }
        });
    }

    private void closeWindows(){
        if(setupWindow!=null){
            setupWindow.setVisible(false);
            setupWindow.dispose();
            activeWindows--;
        }
        if(welcomeWindow!=null){
            welcomeWindow.setVisible(false);
            welcomeWindow.dispose();
            activeWindows--;
        }
    }


    protected void setIsConnected(boolean state){
        this.isConnected = state;
    }
    protected  boolean getIsConnected(){
        return isConnected;
    }

    protected void setListener(ClientListener listener){
        this.listener = listener;
    }
}
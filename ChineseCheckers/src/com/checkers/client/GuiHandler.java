package com.checkers.client;

import javax.swing.*;

public class GuiHandler {

    GuiHandler(){
        start();
    }


    protected void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final GuiWelcome welcomeWindow = new GuiWelcome(GuiHandler.this);
                welcomeWindow.setVisible(true);
            }
        });
    }

    public void newGameSetup(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final GuiSetUp setUpWindow = new GuiSetUp(GuiHandler.this);
                setUpWindow.setVisible(true);
            }
        });
    }

    public  void joinGame(){

    }

    public void newGame(){

    }


}
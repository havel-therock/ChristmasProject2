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
                welcomeWindow = new GuiWelcome(client);
                welcomeWindow.setVisible(true);
                client.setWelcomeWindow(welcomeWindow);
            }
        });
    }

    protected void createGuiSetup(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setupWindow = new GuiSetup(client);
                setupWindow.setVisible(true);
                client.setSetupWindow(setupWindow);
            }
        });
    }

    protected void createBoardWindow(){                        //dummy method
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameWindow = new GuiGame(client);
                gameWindow.setVisible(true);
                client.setGameWindow(gameWindow);
            }
        });
    }

}
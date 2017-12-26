package com.checkers.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CheckersClient{

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientListener listener;
    private GuiWelcome welcomeWindow;
    private GuiSetup setupWindow;
    private GuiGame gameWindow;
    private int activeWindows;
    private boolean[] windowsArray;

    public static void main(String[] args){

        CheckersClient client = new CheckersClient();
        client.initialize();
    }

    private void initialize(){
        windowsArray = new boolean[]{false,false,false};
        createGuiWelcome();
    }

    protected boolean connectServer(String hostName){

        try{

            socket = new Socket(hostName, 4444);
            InputStreamReader inStream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inStream);
            writer = new PrintWriter(socket.getOutputStream());

            listener = new ClientListener(this,reader,writer,windowsArray);

            welcomeWindow.setListener(listener);
            listener.setWelcomeWindow(welcomeWindow);

            System.out.println("Successfully connected to server on ip: "+ hostName);
            return true;

        }catch(IOException e){

            System.out.println("Failed while connecting to server");
            return false;
        }
    }

    protected void createGuiWelcome(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                welcomeWindow = new GuiWelcome(CheckersClient.this);
                welcomeWindow.setVisible(true);
                activeWindows++;
                windowsArray[0]=true;
                System.out.println("New welcome window created");
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
                    windowsArray[1]=true;
                    System.out.println("New setup window created");
                }
            }
        });
    }

    protected void createBoardWindow(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameWindow = new GuiGame(listener);
                gameWindow.setVisible(true);
                listener.setGameWindow(gameWindow);
                activeWindows++;
                closeWindows();
                windowsArray[2]=true;
                System.out.println("New game window created");
            }
        });
    }

    private void closeWindows(){

        if(setupWindow!=null){
            setupWindow.setVisible(false);
            setupWindow.dispose();
            setupWindow = null;
            activeWindows--;
        }
        if(welcomeWindow!=null){
            welcomeWindow.setVisible(false);
            welcomeWindow.dispose();
            welcomeWindow = null;
            activeWindows--;
        }
        windowsArray[0]=false;
        windowsArray[1]=false;
    }

    protected int getActiveWindows(){
        return activeWindows;
    }

    protected  void setActiveWindows(int number){
        this.activeWindows = number;
    }
}

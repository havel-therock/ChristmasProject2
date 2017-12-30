package com.checkers.client;

import java.io.BufferedReader;
import java.io.PrintWriter;

class CheckersClient {

    private BufferedReader reader;
    private PrintWriter writer;
    private  GuiSetup setupWindow;
    private  GuiWelcome welcomeWindow;
    private  GuiGame gameWindow;
    private ClientMain client;
    private Thread listenerThread;
    private ClientListener clientListener;
    private boolean[] windowsArray;

    CheckersClient(ClientMain client, BufferedReader reader, PrintWriter writer, boolean [] windowsArray, GuiWelcome welcomeWindow){

        this.client = client;
        this.reader = reader;
        this.writer = writer;
        this.windowsArray = windowsArray;
        this.welcomeWindow = welcomeWindow;

        create();
    }

    public void create() {
        clientListener = new ClientListener(this,reader,welcomeWindow,setupWindow,gameWindow);
        listenerThread = new Thread(clientListener);
        listenerThread.start();
    }

    protected void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }

    protected void newGameCmd(String line){

        client.createBoardWindow();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameWindow.showMessage(line);
    }

    protected void joinGameCmd(String line){

        client.createBoardWindow();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameWindow.showMessage(line);
    }

    protected void quit( int number){

        if(client.getActiveWindows()==1) {
            sendMessage("exit");
            clientListener.setEnd();
        }else{
            client.setActiveWindows(client.getActiveWindows()-1);
        }

        if(number == 0) {
            welcomeWindow.setVisible(false);
            welcomeWindow.dispose();
            welcomeWindow = null;
            windowsArray[0]=false;
        }else if(number == 1) {
            setupWindow.setVisible(false);
            setupWindow.dispose();
            setupWindow = null;
            windowsArray[1]=false;
        }else if (number == 2){
            gameWindow.setVisible(false);
            gameWindow.dispose();
            gameWindow = null;
            windowsArray[2]=false;
        }

    }

    protected void handleServerClosing(){
        sendMessage("ok");
        showMessage("Connection with server has been lost :/ ");
        if (windowsArray[2]) {
            quit(2);
        }
        if (windowsArray[1]){
            quit(1);
        }
        if(windowsArray[0]){
           quit(0);
        }

        newWelcomeWindow();
        clientListener.setEnd();
    }

    protected void showMessage(String message) {

        if (windowsArray[2]) {
            gameWindow.showMessage(message);
        }else if (windowsArray[1]){
            setupWindow.showMessage(message);
        }else if(windowsArray[0]){
            welcomeWindow.showMessage(message);
        }
    }

    protected void newWelcomeWindow(){

        client.createGuiWelcome();
    }

    protected void setSetupWindow(GuiSetup setupWindow){
        this.setupWindow = setupWindow;
    }
    protected void setGameWindow(GuiGame gameWindow){
        this.gameWindow = gameWindow;
    }
}

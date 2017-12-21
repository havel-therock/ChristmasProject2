package com.checkers.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

class ClientListener {

    BufferedReader reader;
    PrintWriter writer;
    protected  GuiSetup setupWindow;
    protected  GuiWelcome welcomeWindow;
    protected  GuiGame gameWindow;
    protected GuiHandler handler;
    protected Thread t;
    protected Listening listener;

    ClientListener(BufferedReader reader,PrintWriter writer, GuiHandler handler ,GuiWelcome welcomeWindow){

        this.reader = reader;
        this.handler = handler;
        this.writer = writer;
        this.welcomeWindow = welcomeWindow;
        create();

    }

    public void create() {
        listener = new Listening(this);
        t = new Thread(listener);
        t.start();
    }


    protected void newGameCmd(String line){
        handler.createBoardWindow();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameWindow.showMessage(line);
    }

    protected void joinGameCmd(String line){
        handler.createBoardWindow();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameWindow.showMessage(line);

    }
    protected void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }

    protected void quit(){
            if(handler.activeWindows==1) {
                sendMessage("exit");
                listener.setEnd(true);

            }else{
                handler.activeWindows--;
            }
    }

    void setGameWindow(GuiGame gameWindow){
        this.gameWindow=gameWindow;
    }

    void setSetupWindow(GuiSetup setupWindow){
        this.setupWindow=setupWindow;
    }


    GuiHandler getGuiHandler(){
        return handler;
    }
}

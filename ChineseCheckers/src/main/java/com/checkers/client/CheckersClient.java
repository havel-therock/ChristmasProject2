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
    GuiWelcome welcomeWindow;



    public static void main(String[] args){
        CheckersClient client = new CheckersClient();
        client.createHandler();
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
            listener = new ClientListener(reader,writer,handler,welcomeWindow);
            handler.setListener(listener);
            welcomeWindow.setListener(listener);
            return true;
        }catch(IOException e){
            System.out.println("Failed while connecting server");
            return false;
        }
    }

    void setWelcomeWindow(GuiWelcome welcomeWindow){
        this.welcomeWindow=welcomeWindow;
    }
}

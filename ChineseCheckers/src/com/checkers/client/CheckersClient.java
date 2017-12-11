package com.checkers.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CheckersClient {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public static void main(String[] args){
        CheckersClient client = new CheckersClient();
        client.connectServer();
        GuiHandler handler = new GuiHandler();
    }

    private void connectServer(){
        try{
            socket = new Socket("127.0.0.1", 4444);
            InputStreamReader inStream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inStream);
            writer = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Failed while connecting server");
        }

    }
}

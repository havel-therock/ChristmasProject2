package com.checkers.client;

import java.io.BufferedReader;
import java.io.IOException;

class ClientListener implements Runnable{

    BufferedReader reader;

    ClientListener(BufferedReader reader){
        this.reader = reader;
    }

    public void run() {
        listen();
    }

    protected void listen(){
        while(true){
            try{
                String serverLine = null;
                while(serverLine == null) {
                    serverLine = reader.readLine();
                    System.out.println(serverLine);
                    //parseLine(serverLine);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}

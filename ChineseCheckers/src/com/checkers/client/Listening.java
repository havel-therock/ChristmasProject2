package com.checkers.client;

import java.io.IOException;


class Listening  implements Runnable  {

ClientListener listener;

    Listening(ClientListener listener){

      this.listener = listener;
    }

    public void run() {
      while(true){
          listen();
      }
    }

    protected void parseLine(String line){
        //System.out.println(line);
        switch (line){
            case "Game already exists":
                listener.setupWindow.showMessage(line);
                break;

            case "Successfully created a game":
                listener.newGameCmd(line);
                break;

            case "Successfully joined the game":
                listener.joinGameCmd(line);
                break;

            case "Game does not exist":
               listener.welcomeWindow.showMessage(line);
                break;
            case "Error 37":
                listener.setupWindow.showMessage(line);


            default:
                break;
        }
    }


    protected void listen(){
        while(true){
            try{
                String serverLine = null;
                while(serverLine == null) {
                    serverLine = listener.reader.readLine();
                    System.out.println(serverLine);
                    parseLine(serverLine);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


}

package com.checkers.client;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class ClientMain {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private CheckersClient checkersClient;
    private GuiWelcome welcomeWindow;
    private GuiSetup setupWindow;
    private GuiGame gameWindow;
    private int activeWindows;
    private boolean[] windowsArray;

    public static void main(String[] args){

        ClientMain client = new ClientMain();
        client.initialize();
    }

    private void initialize(){
        windowsArray = new boolean[]{false,false,false};
        createGuiWelcome();
    }

    protected boolean connectServer(String hostName){

        try{

            socket = openSocket(hostName, 4444);

            InputStreamReader inStream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inStream);
            writer = new PrintWriter(socket.getOutputStream());

            checkersClient = new CheckersClient(this,reader,writer,windowsArray,welcomeWindow);

            welcomeWindow.setCheckersClient(checkersClient);

            System.out.println("Successfully connected to server on ip: "+ hostName);
            return true;

        } catch (IOException e) {
            System.out.println("Failed while connecting to server");
            return false;
        }catch (Exception ex) {
            System.out.println ("Failed while connecting to server");
            return false;
        }
    }

    private Socket openSocket(String hostname,int port) throws Exception{
        Socket socket;
        try
        {
            SocketAddress socketAddress = new InetSocketAddress(hostname, port);

            socket = new Socket();
            socket.connect(socketAddress, 3000);
            return socket;
        }
        catch (SocketTimeoutException exc)
        {
            throw exc;
        }
    }

    protected void createGuiWelcome(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                welcomeWindow = new GuiWelcome(ClientMain.this);
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
                    setupWindow = new GuiSetup(checkersClient);
                    setupWindow.setVisible(true);
                    checkersClient.setSetupWindow(setupWindow);
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
                gameWindow = new GuiGame(checkersClient);
                //gameWindow.setVisible(true);
                checkersClient.setGameWindow(gameWindow);
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

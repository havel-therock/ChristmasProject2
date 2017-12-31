package com.checkers.client;

import com.checkers.server.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    //private JLabel example;
    private int[][] gameBoard;
    private ClientBoard board;
    private CheckersClient checkersClient;


    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

       // createComponents();
       // addComponents();
      //  addListeners();


      //  setLocation(400,20);
       // pack();

    }


    public void setBoard(String[] gameBoard) {
        String gameFieldsString = "";
        String[] gameFields;
        int[] gameFieldsInt;
        int height = (gameBoard.length - 1);
        int length = 0;
        int[] fieldsPerRow = new int[height];
        for(int i = 1; i < gameBoard.length; i++){
            fieldsPerRow[i - 1] = gameBoard[i].length() - gameBoard[i].replace(",","").length();
        }
        for(int i = 0; i < height; i++){
            //System.out.println("fields per " + i + " row: " + fieldsPerRow[i]);
            if(fieldsPerRow[i] > length){
                length = fieldsPerRow[i];
            }
            gameFieldsString = gameFieldsString + gameBoard[i+1];
        }

        length = length*2 - 1;
        System.out.println(length);
        this.gameBoard = new int[height][length];
        for(int i=0;i<height;i++){
            for(int j=0;j<length;j++){
                this.gameBoard[i][j]= Board.NOT_PLAYABLE_FIELD;
            }
        }

        gameFields = gameFieldsString.split(",");
        gameFieldsInt = new int[gameFields.length];
        for(int i = 0; i < gameFields.length; i++){
            gameFieldsInt[i] = Integer.parseInt(gameFields[i]);
        }
        int fieldId = -1;
        for(int i = 0; i < height; i++){
            int fields = fieldsPerRow[i];
            int verticalCenter = length/2 + 1;
            int start = verticalCenter - fields;
            for(int j=0;j<fields;j++){
                fieldId++;
                this.gameBoard[i][start] = gameFieldsInt[fieldId];
                start = start + 2;
            }
        }
        Board.displayboard2(this.gameBoard, height, length);
        createComponents();
        addComponents();
        addListeners();
        setLocation(400,20);
        pack();
    }

    void createComponents() {
            board = new ClientBoard(gameBoard);
    }

     void addComponents(){
        add(board);
    }

    void addListeners(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                checkersClient.quit(2);
            }
        });
    }
    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiGame.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }

    void boardCmd(String line){

        String[] arguments = line.split(";");
        switch (arguments[0]){
            case "boardSetUp":
                setBoard(arguments);
                break;
            case "boardMove":
                //moveBoard(arguments);
                break;
            default:
                break;
        }
    }
}

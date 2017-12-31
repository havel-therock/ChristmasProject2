package com.checkers.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel example;
    private Board board;
    private String[][] gameBoard;
    private CheckersClient checkersClient;


    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


        gameBoard = new String[17][17];
        for(int i=0;i<17;i++){
            for(int j=0;j<17;j++){
                gameBoard[i][j]="-1";
            }
        }
        gameBoard[0][8]="1";
        gameBoard[1][7]="1";
        gameBoard[1][9]="1";
        gameBoard[2][6]="1";
        gameBoard[2][8]="1";
        gameBoard[2][10]="1";
        gameBoard[8][0]="2";
        gameBoard[8][16]="2";
        gameBoard[16][8]="3";
        gameBoard[10][10]="4";
        gameBoard[13][13]="5";
        gameBoard[5][5]="6";


        for(int i=0;i<17;i++){
            for(int j=0;j<17;j++){
               System.out.print(gameBoard[i][j]+" ");
            }
            System.out.println();
        }


        createComponents();
        addComponents();
        addListeners();


        setLocation(400,20);
        pack();

    }


    void createComponents() {
        board = new Board(gameBoard);

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
}

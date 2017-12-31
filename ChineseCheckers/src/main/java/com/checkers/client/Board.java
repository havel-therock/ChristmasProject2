package com.checkers.client;

import javax.swing.*;

import java.awt.*;

public class Board extends JPanel {

    private String[][] gameBoard;
    private int maxTiles;
    private int edgeSize;

    Board(String[][] gameBoard){
        this.gameBoard = gameBoard;
        setBounds();
        setBackground(Color.lightGray);
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);


        int row =0;
        int col = 0;
        for(int i = 0; i < maxTiles; i++){
            for(int j = 0; j < maxTiles; j++){
                if(!gameBoard[i][j].equals("-1")){
                    drawCircle(g,col,row,gameBoard[i][j]);
                }
                col=col+edgeSize/maxTiles;
            }
            col=0;
            row=row+edgeSize/maxTiles;
        }

    }

    private void setBounds(){
        this.maxTiles = gameBoard.length;
        System.out.println("length is "+maxTiles);

        Dimension screenSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double minimum = Math.min(width,height);
        this.edgeSize = (int)(minimum*0.8);
        this.setPreferredSize(new Dimension(edgeSize,edgeSize));
    }

    private void drawCircle(Graphics g,int col,int row,String color){
        g.drawOval(col,row,edgeSize/maxTiles,edgeSize/maxTiles);
        g.setColor(selectColor(color));
        g.fillOval(col,row,edgeSize/maxTiles,edgeSize/maxTiles);
        g.setColor(Color.black);
    }
    private Color selectColor(String color) {

        switch (color){
            case "1" :
                return Color.blue;
            case "2":
                return Color.red;
            case "3" :
                return Color.yellow;
            case "4" :
                return Color.green;
            case "5" :
                return Color.orange;
            case "6":
                return  Color.magenta;
            default:
                    return Color.black;
        }

    }

}

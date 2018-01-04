package com.checkers.client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class BoardPanel extends JPanel {

    protected int[][] gameBoard;
    private int edgeSize;
    private int maxWidth,maxHeight;
    private String firstCircle, secondCircle;
    private boolean isFirst,isSecond;

    BoardPanel(int[][] gameBoard){
        this.gameBoard = gameBoard;
        isFirst = false;
        isSecond = false;
        setBounds();
        addListeners();
        setBackground(Color.white);
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);


        int row =0;
        int col = 0;
        for(int i = 0; i < maxHeight; i++){
            for(int j = 0; j < maxWidth; j++){
                if(gameBoard[i][j] != -1){
                    drawCircle(g,col,row,gameBoard[i][j]);
                    drawSelection(g,i,j,col,row);
                }
                col=col+edgeSize/maxWidth;
            }
            col=0;
            row=row+edgeSize/maxHeight;
        }

    }

    private void setBounds(){
        maxWidth = gameBoard[0].length;
        maxHeight = gameBoard.length;

        System.out.println("length is "+maxWidth+"x"+maxHeight);

        Dimension screenSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double minimum = Math.min(width,height);
        this.edgeSize = (int)(minimum*0.7);
        this.setPreferredSize(new Dimension(edgeSize,edgeSize));
    }

    private void drawCircle(Graphics g,int col,int row,int color){
        g.setColor(selectColor(color));
        if(color != -1) {
            g.setColor(Color.black);
        }
        g.drawOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
        g.setColor(selectColor(color));
        g.fillOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
    }
    protected Color selectColor(int color) {

        switch (color){
            case 0:
                return Color.white;
            case 1:
                return Color.blue;
            case 2:
                return Color.red;
            case 3:
                return Color.cyan;
            case 4:
                return Color.orange;
            case 5:
                return Color.green;
            case 6:
                return  Color.magenta;
            default:
                return Color.black;
        }
    }

    private void drawSelection(Graphics g, int i, int j,int col,int row){
        Color tmp = g.getColor();
        float thickness = 2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(thickness));

        if(isFirst){
            String arguments[] = firstCircle.split(";");
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            if(x==i&&y==j){
                g2.setColor(tmp.darker().darker());
                g2.fillOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
                g2.setColor(Color.black);
                g2.drawOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
                g2.drawString("Start",col,row-1);
                System.out.println("first circle"+firstCircle+ "colored");
            }
        }
        if(isSecond){
            String arguments[] = secondCircle.split(";");
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            if(x==i&&y==j){
                g2.setColor(tmp.darker().darker());
                g2.fillOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
                g2.setColor(Color.black);
                g2.drawOval(col,row,edgeSize/maxWidth,edgeSize/maxWidth);
                g2.drawString("End",col,row-1);
                System.out.println("second circle"+secondCircle+ "colored");
            }
        }

    }

    private void addListeners(){

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event){
                Point tmp = event.getPoint();
                setCoordinates(tmp);
                if(firstCircle!=null)
                    System.out.println("first circle"+firstCircle);
                if(secondCircle!=null)
                    System.out.println("second circle"+secondCircle);
            }
        });
    }


    private void setCoordinates(Point point){
        double x = point.getX();
        double y = point.getY();
        int row =0;
        int col = 0;

        for(int i = 0; i < maxHeight; i++){
            for(int j = 0; j < maxWidth; j++){
                    if (gameBoard[i][j] != -1) {
                        if(y>row&&y<(row+edgeSize/maxWidth) && x>col&&x<(col+edgeSize/maxWidth)){

                            if(!isFirst){
                                if(isSecond){
                                    String arguments2[] = secondCircle.split(";");
                                    if(i == Integer.parseInt(arguments2[0]) && j == Integer.parseInt(arguments2[1])){
                                        return;
                                    }
                                }
                                firstCircle = i+";"+j+";";
                                isFirst = true;
                                repaint();
                                return;
                            }else if(!isSecond){
                                String arguments1[] = firstCircle.split(";");
                                if(i == Integer.parseInt(arguments1[0]) && j == Integer.parseInt(arguments1[1])){
                                        return;
                                }
                                secondCircle = i+";"+j;
                                isSecond = true;
                                repaint();
                                return;
                            }

                            String arguments2[] = secondCircle.split(";");
                            String arguments1[] = firstCircle.split(";");


                                if((Integer.parseInt(arguments1[0])==i)&&(Integer.parseInt(arguments1[1])==j)){
                                    isFirst = false;
                                    repaint();
                                }


                                if((Integer.parseInt(arguments2[0])==i)&&(Integer.parseInt(arguments2[1])==j)){
                                    isSecond = false;
                                    repaint();
                                }

                            return;
                        }
                    }
                col=col+edgeSize/maxWidth;
            }
            col=0;
            row=row+edgeSize/maxHeight;
        }
    }

    protected boolean isMoveReady(){
        if(isFirst&&isSecond){
            return true;
        }else{
            return false;
        }
    }

    protected String getCircle(int number){
        if(number==1){
            return firstCircle;
        }else if(number==2){
            return secondCircle;
        }else return "invalid circle number";
    }

    protected void move(String move){
        String arguments [] = move.split(";");
        int x1,x2,y1,y2,tmp;
        x1 = Integer.parseInt(arguments[1]);
        y1 = Integer.parseInt(arguments[2]);
        x2 = Integer.parseInt(arguments[3]);
        y2= Integer.parseInt(arguments[4]);
        tmp =  gameBoard[x1][y1];
        gameBoard[x1][y1] = gameBoard [x2][y2];
        gameBoard [x2][y2] = tmp;
        repaint();
    }

}

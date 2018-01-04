package com.checkers.client;

import com.checkers.server.board.Board;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel playerName;
    private JButton help,send,quit,next,addBot,deleteBot;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextArea textArea;
    private BoardPanel board;
    private int[][] gameBoard;
    private CheckersClient checkersClient;
    private JScrollPane scroll;

    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;
        this.setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    }


    private void setupBoard(String[] gameBoard) {

        createBoard(gameBoard);

        createComponents();
        addComponents();
        addListeners();
        setLocation(400,20);

        pack();
        setVisible(true);
    }

    private void resetBoard(String [] gameBoard){
        createBoard2(gameBoard);
        board.repaint();
    }

    private void createBoard2(String[] gameBoard) {
        int tmp[][];
        String gameFieldsString = "";
        String[] gameFields;
        int[] gameFieldsInt;
        int height = (gameBoard.length - 1);
        int length = 0;
        int[] fieldsPerRow = new int[height];
        for (int i = 1; i < gameBoard.length; i++) {
            fieldsPerRow[i - 1] = gameBoard[i].length() - gameBoard[i].replace(",", "").length();
        }
        for (int i = 0; i < height; i++) {
            if (fieldsPerRow[i] > length) {
                length = fieldsPerRow[i];
            }
            gameFieldsString = gameFieldsString + gameBoard[i + 1];
        }

        length = length * 2 - 1;
        System.out.println(length);
        tmp = new int[height][length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                tmp[i][j] = Board.NOT_PLAYABLE_FIELD;
            }
        }

        gameFields = gameFieldsString.split(",");
        gameFieldsInt = new int[gameFields.length];
        for (int i = 0; i < gameFields.length; i++) {
            gameFieldsInt[i] = Integer.parseInt(gameFields[i]);
        }
        int fieldId = -1;
        for (int i = 0; i < height; i++) {
            int fields = fieldsPerRow[i];
            int verticalCenter = length / 2 + 1;
            int start = verticalCenter - fields;
            for (int j = 0; j < fields; j++) {
                fieldId++;
                tmp[i][start] = gameFieldsInt[fieldId];
                start = start + 2;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                this.gameBoard[i][j] = tmp[i][j];
            }
        }
    }

    private void createBoard(String[] gameBoard) {

        String gameFieldsString = "";
        String[] gameFields;
        int[] gameFieldsInt;
        int height = (gameBoard.length - 1);
        int length = 0;
        int[] fieldsPerRow = new int[height];
        for (int i = 1; i < gameBoard.length; i++) {
            fieldsPerRow[i - 1] = gameBoard[i].length() - gameBoard[i].replace(",", "").length();
        }
        for (int i = 0; i < height; i++) {
            if (fieldsPerRow[i] > length) {
                length = fieldsPerRow[i];
            }
            gameFieldsString = gameFieldsString + gameBoard[i + 1];
        }

        length = length * 2 - 1;
        System.out.println(length);
        this.gameBoard = new int[height][length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                this.gameBoard[i][j] = Board.NOT_PLAYABLE_FIELD;
            }
        }

        gameFields = gameFieldsString.split(",");
        gameFieldsInt = new int[gameFields.length];
        for (int i = 0; i < gameFields.length; i++) {
            gameFieldsInt[i] = Integer.parseInt(gameFields[i]);
        }
        int fieldId = -1;
        for (int i = 0; i < height; i++) {
            int fields = fieldsPerRow[i];
            int verticalCenter = length / 2 + 1;
            int start = verticalCenter - fields;
            for (int j = 0; j < fields; j++) {
                fieldId++;
                this.gameBoard[i][start] = gameFieldsInt[fieldId];
                start = start + 2;
            }
        }
    }

    private void setInfo(String [] arguments){
        Color tmpColor = board.selectColor(Integer.parseInt(arguments[1]));
        Font tmp = new Font("Comic Sans MS",Font.BOLD,15);
        playerName.setFont(tmp);
        playerName.setForeground(tmpColor);
        playerName.setText("  Hello "+arguments[2]+", your're playing game: "+arguments[3]+"  ");
    }

    private void createComponents() {
        help = new JButton("Help");
        send = new JButton("Send");
        quit = new JButton("Exit");
        next = new JButton("Next");
        addBot = new JButton("Add new bot");
        deleteBot = new JButton("Delete bot");
        board = new BoardPanel(gameBoard);
        topPanel = new JPanel();
        playerName = new JLabel();
        bottomPanel = new JPanel();
        textArea = new JTextArea(3,50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBorder(new TitledBorder("server info:"));
        scroll = new JScrollPane(textArea);
        topPanel.setBorder(new TitledBorder(""));
    }

    private void addComponents(){

        bottomPanel.add(textArea);

        topPanel.add(quit);
        topPanel.add(help);
        topPanel.add(playerName);
        topPanel.add(send);
        topPanel.add(next);
        topPanel.add(addBot);
        topPanel.add(deleteBot);


        add(board,BorderLayout.CENTER);
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel,BorderLayout.SOUTH);
    }

    private void addListeners(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                checkersClient.quit(2);
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersClient.quit(2);
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("Select two pieces and click on send button to execute the move; \n" +
                        "Click again on a selected piece to deselect it and use it again; \n" +
                        "Click next button to omit your move \n"+
                        "Click on exit if you want to quit the game \n"+
                        "Careful! If the game has started you won't be able to join \n"+
                        "The first player in the opposite corner wins! ");
            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(board.isMoveReady()){
                    checkersClient.sendMessage("move;"+board.getCircle(1)+board.getCircle(2));    //move +indeksy pól w tablicy oddzielone ;
                }else{
                    showMessage("Please select a valid move (with start and end)");
                }
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersClient.sendMessage("next;");
            }
        });

        addBot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersClient.sendMessage("addbot");
            }
        });
        deleteBot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersClient.sendMessage("deletebot");
            }
        });
    }
    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiGame.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }

    protected void writeMessage(String line){
       textArea.setText(line);
    }

    private String getColor(String i){
        switch (i){
            case "1":
                return "Blue";
            case "2":
                return "Red";
            case "3":
                return "Cyan";
            case "4":
                return "Orange";
            case "5":
                return "Green";
            case "6":
                return  "Magenta";
            default:
                return "Black";
        }
    }

    protected void cmd(String line) {

        String[] arguments = line.split(";");
        switch (arguments[0]) {
            case "boardSetUp":
                setupBoard(arguments);
                break;
            case "move":
                board.move(line);
                break;
            case "info":
                setInfo(arguments);
                break;
            case "boardReset":
                resetBoard(arguments);
                break;
            case "won":
                showMessage(getColor(arguments[1])+" player won");
                break;

            default:
                break;
        }
    }

    protected void endGame(){
        help.setEnabled(false);
        send.setEnabled(false);
        next.setEnabled(false);
        board.setEnabled(false);
    }

}

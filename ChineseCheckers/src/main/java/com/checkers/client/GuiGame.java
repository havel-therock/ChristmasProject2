package com.checkers.client;

import com.checkers.server.board.Board;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel playerName;
    private JButton help,send;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextArea textArea;
    private BoardPanel board;
    private int[][] gameBoard;
    private CheckersClient checkersClient;
    private int edgeSize;
    private String myColor;
    private JScrollPane scroll;

    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;
        this.setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

        createComponents();
        addComponents();
        addListeners();
        setLocation(400,20);
        board.setMaximumSize(new Dimension(edgeSize,edgeSize));
        pack();
        setVisible(true);
        setBounds();
    }

    private void setBounds() {
        Dimension screenSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double minimum = Math.min(width, height);
        edgeSize = (int) (minimum * 0.7);
    }

    void createComponents() {
        help = new JButton("Help");
        send = new JButton("Send");
        board = new BoardPanel(gameBoard);
        topPanel = new JPanel();
        playerName = new JLabel(myColor);
        bottomPanel = new JPanel();
        textArea = new JTextArea(3,50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBorder(new TitledBorder("server info:"));
        scroll = new JScrollPane(textArea);
        topPanel.setBorder(new TitledBorder(""));
    }

     void addComponents(){

        bottomPanel.add(textArea);

        topPanel.add(help);
        topPanel.add(playerName);
        topPanel.add(send);


        add(board,BorderLayout.CENTER);
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel,BorderLayout.SOUTH);
    }

    void addListeners(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                checkersClient.quit(2);
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("Swing to jakiś kurwa żart");
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
    }
    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiGame.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }

    void boardCmd(String line) {

        String[] arguments = line.split(";");
        switch (arguments[0]) {
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

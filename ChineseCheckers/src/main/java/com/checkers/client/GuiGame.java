package com.checkers.client;



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
    private String[][] gameBoard;
    private CheckersClient checkersClient;
    private int edgeSize;
    private String myColor;
    private JScrollPane scroll;

    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        myColor = "blue";   //przed setBoard
        setBoard();
        //board.setMyColor(myColor);



        setBounds();

        createComponents();
        addComponents();
        addListeners();


        setLocation(400,20);
        board.setMaximumSize(new Dimension(edgeSize,edgeSize));

        pack();
        setVisible(true);


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

    private void setBoard(){
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
    }
}

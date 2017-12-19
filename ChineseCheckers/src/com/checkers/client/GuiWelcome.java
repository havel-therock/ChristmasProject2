package com.checkers.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiWelcome extends JFrame {

    private JButton newGame;
    private JButton joinGame;
    private JButton info;
    private JButton quit;
    private JButton ip;
    private JTextField gameText;
    private JTextField ipText;
    private JPanel bodyContainer;
    private JPanel container;
    private JLabel welcome;
    private GridBagConstraints constraints;
    private String message;
    private boolean isConnected;
    
    private CheckersClient client;


    GuiWelcome(CheckersClient client){

        super("Welcome menu");
        this.client=client;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        message = " Chinese Checkers \n Kacper Szatan i Piotr Borowczyk";
        isConnected = false;

        createComponents();
        setContainer();
        addComponents();
        addListeners();

        setSize(300, 400);
        setLocation(400,400);

    }



    private void setContainer(){
        add(container);
        container.setBorder(new EmptyBorder(10,10,10,10));
        container.setLayout(new GridBagLayout());

        bodyContainer.setLayout(new GridBagLayout());

    }

    private void createComponents(){
        container = new JPanel();
        bodyContainer = new JPanel();
        welcome = new JLabel("Chinese Checkers");
        welcome.setFont(new Font("Comic Sans MS", Font.PLAIN | Font.BOLD, 30));
        newGame = new JButton("New Game");
        joinGame = new JButton("Join Game");
        info = new JButton("Info");
        quit = new JButton("Exit");
        ip = new JButton("Enter ip and press to connect:");
        ipText = new JTextField("127.0.0.1");
        gameText = new JTextField("");
    }

    private void addComponents(){

        constraints = new GridBagConstraints();

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.NORTH;
        container.add(welcome,constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;

        constraints.insets = new Insets(40,0,40,0);
        container.add(bodyContainer,constraints);
        constraints.insets= new Insets(0,0,0,0);

        bodyContainer.add(ip,constraints);
        bodyContainer.add(ipText,constraints);
        bodyContainer.add(newGame,constraints);
        bodyContainer.add(joinGame,constraints);
        bodyContainer.add(gameText,constraints);
        bodyContainer.add(info,constraints);
        bodyContainer.add(quit,constraints);


    }

    private void addListeners(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.quit();
                GuiWelcome.this.setVisible(false);
                GuiWelcome.this.dispose();
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected) {
                    client.getGuiHandler().createGuiSetup();
                }
            }
        });

        joinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected) {
                    client.joinGame(gameText.getText());
                }
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GuiWelcome.this , message,"Info",JOptionPane.PLAIN_MESSAGE);
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.quit();
                GuiWelcome.this.setVisible(false);
                GuiWelcome.this.dispose();
            }
        });

        ip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isConnected) {
                    isConnected=client.connectServer(ipText.getText());
                    if(isConnected) {
                        ipText.setText("Connected");
                    }
                }
            }
        });
    }

    void showMessage(String message){
        JOptionPane.showMessageDialog(GuiWelcome.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }



}
package com.checkers.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel example;
    private String[][] GameBoard;
    private CheckersClient checkersClient;


    GuiGame (CheckersClient listener){
        super("Game in progress");
        this.checkersClient = listener;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComponents();
        addComponents();
        addListeners();

        setPreferredSize(new Dimension(300,200));
        setLocation(400,300);
        pack();
    }

    void createComponents(){
        example = new JLabel("example");
    }

     void addComponents(){
        add(example);
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

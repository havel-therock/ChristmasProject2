package com.checkers.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel example;
    private ClientListener listener;


    GuiGame (ClientListener listener){
        super("Game in progress");
        this.listener = listener;

        setPreferredSize(new Dimension(300,200));
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
                listener.quit(3);
                GuiGame.this.setVisible(false);
                GuiGame.this.dispose();

            }
        });
    }
    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiGame.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }
}

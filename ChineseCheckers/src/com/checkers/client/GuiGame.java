package com.checkers.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiGame extends JFrame {

    private JLabel example;

    GuiGame (){
        super("Game in progress");

        setPreferredSize(new Dimension(300,200));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComponents();
        addComponents();
        addListeners();

        setPreferredSize(new Dimension(300,200));
        setLocation(400,300);
        pack();
    }

    private void createComponents(){
        example = new JLabel("example");
    }

    private void addComponents(){
        add(example);
    }

    private void addListeners(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GuiGame.this.setVisible(false);
                GuiGame.this.dispose();
            }
        });
    }
}

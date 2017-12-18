package com.checkers.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiSetup extends JFrame{

    private GuiHandler handler;
    private JTabbedPane tabbs;
    private JButton create;
    private JButton clear;
    private JLabel pieces, players, gameName,corners,cornerWidth;
    private JPanel basics,extras,container;
    private JTextField gameNameTxt, playersTxt,piecesTxt,cornersTxt,cornerWidthTxt;

    GuiSetup(GuiHandler handler){

        super("Setup menu");
        this.handler=handler;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComponents();
        addComponents();
        addListeners();

        setLocation(600,250);
        pack();

        setResizable(false);
    }

    private void createComponents(){
        container = new JPanel();
        tabbs = new JTabbedPane();
        basics = new JPanel();
        extras = new JPanel();
        create = new JButton("Play!");
        clear = new JButton("Clear");
        pieces = new JLabel("Number of pieces: ");
        players = new JLabel("Number of players: ");
        gameName = new JLabel("Name of the game: ");
        corners = new JLabel("Number of corners: ");
        cornerWidth = new JLabel("Number of corners: ");
        gameNameTxt = new JTextField();
        playersTxt = new JTextField("3");
        piecesTxt = new JTextField("10");
        cornersTxt = new JTextField("6");
        cornerWidthTxt = new JTextField("4");
    }

    private void addComponents(){

        basics.setLayout(new GridLayout(3,2));
        extras.setLayout(new GridLayout(3,2));

        basics.add(gameName);
        basics.add(gameNameTxt);
        basics.add(players);
        basics.add(playersTxt);
        basics.add(create);
        basics.add(clear);

        extras.add(pieces);
        extras.add(piecesTxt);
        extras.add(corners);
        extras.add(cornersTxt);
        extras.add(cornerWidth);
        extras.add(cornerWidthTxt);

        add(container);
        container.setBorder(new EmptyBorder(5,5,5,5));
        container.add(tabbs);
        tabbs.addTab("Setup", null, basics, "Basic game setup");
        tabbs.addTab("Extras", null, extras, "Extra game options");

        basics.setPreferredSize(new Dimension(300, 150));
        container.setPreferredSize(new Dimension(350,200));
    }

    private String parseData(){
        String tmp = gameNameTxt.getText()+";"+playersTxt.getText()+";"+
                piecesTxt.getText()+";"+cornersTxt.getText()+";"+cornerWidthTxt.getText();
        return tmp;
    }

    private void addListeners(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GuiSetup.this.setVisible(false);
                GuiSetup.this.dispose();
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.newGame(parseData());
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameNameTxt.setText("");
                playersTxt.setText("");
                piecesTxt.setText("");
                cornersTxt.setText("");
                cornerWidthTxt.setText("");
            }
        });


    }

}

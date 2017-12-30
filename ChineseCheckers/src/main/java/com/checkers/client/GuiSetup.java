package com.checkers.client;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiSetup extends JFrame{

    private CheckersClient checkersClient;
    private JTabbedPane tabs;
    private JButton create,clear;
    private JLabel pieces, players, gameName,corners,cornerWidth;
    private JPanel basics,extras,container;
    private JTextField gameNameTxt, playersTxt,piecesTxt,cornersTxt,cornerWidthTxt;

    GuiSetup(CheckersClient checkersClient){

        super("Setup menu");
        this.checkersClient = checkersClient;

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
        tabs = new JTabbedPane();
        basics = new JPanel();
        extras = new JPanel();
        create = new JButton("Play!");
        clear = new JButton("Clear");
        pieces = new JLabel("Number of pieces: ");
        players = new JLabel("Number of players: ");
        gameName = new JLabel("Name of the game: ");
        corners = new JLabel("Number of corners: ");
        cornerWidth = new JLabel("Width of corner: ");
        gameNameTxt = new JTextField();
        playersTxt = new JTextField("3");
        piecesTxt = new JTextField("10");
        cornersTxt = new JTextField("6 (Premium users only)");
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
        container.add(tabs);
        tabs.addTab("Setup", null, basics, "Basic game setup");
        tabs.addTab("Extras", null, extras, "Extra game options");

        basics.setPreferredSize(new Dimension(350, 200));
        container.setPreferredSize(new Dimension(400,260));

        cornersTxt.setEditable(false);
        cornersTxt.setBorder(new EtchedBorder());
        cornersTxt.setBackground(Color.WHITE);
    }

    private String parseData(){
        String tmp = gameNameTxt.getText()+";"+playersTxt.getText()+";"+
                piecesTxt.getText()+";"+"6"+";"+cornerWidthTxt.getText();
        return tmp;
    }

    private void addListeners(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                checkersClient.quit(1);
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersClient.sendMessage("newg;"+parseData());
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

    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiSetup.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }
}

package com.checkers.server;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.*;
import java.io.*;

public class GuiServer extends JFrame {

    CheckersServer server;
    JButton quit;
    JButton connect;
    JTextPane ip;
    JPanel panel;

    GuiServer(CheckersServer server){

        super("Server GUI");
        this.server = server;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComponents();
        setIp();
        addComponents();
        addListeners();

        setLocation(400,300);
        pack();
    }

    void createComponents(){

        panel = new JPanel();
        ip = new JTextPane();
        quit = new JButton("exit");
        connect = new JButton("start server");
    }

    void addComponents(){


        panel.setLayout(new GridLayout(3,1,5,15));

        panel.add(ip);
        panel.add(connect);
        panel.add(quit);
        add(panel);

        panel.setPreferredSize(new Dimension(250,200));

        StyledDocument doc = ip.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        ip.setEditable(false);
        ip.setBackground(null);
        ip.setBorder(null);
        ip.setFont( new Font("Serif", Font.PLAIN, 20));

    }

    void addListeners(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               exit();

            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(server.serverListener == null){
                    server.createListener();
                    connect.setText("server working");
                }
            }
        });
    }

    private void exit(){

        if(server.serverListener != null) {
            if (server.listenerThread.isAlive()) {
                try {
                    server.serverListener.mainSocket.close();
                    server.listenerThread.interrupt();
                    server.listenerThread.join();
                } catch (IOException e1) {
                    System.out.println("cannot close server");
                } catch (InterruptedException ex) {
                    System.out.println("cannot close server");
                }

                for (Player current : server.playerList) {
                    current.writeToPlayer("Server will stop soon");
                    current.connected = false;
                    try {
                        current.listenerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        GuiServer.this.setVisible(false);
        GuiServer.this.dispose();

    }

    public void setIp() {
        try{
        URL myIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(myIp.openStream()));
        String address = in.readLine();
        ip.setText("Server ip is: \n" + address);
        }catch(MalformedURLException e){
            ip.setText("Unknown ip address");
        }catch(IOException ex){
            ip.setText("Unknown ip address");
        }
    }

    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiServer.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }
}



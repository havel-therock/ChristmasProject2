package com.checkers.client;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GuiWelcome extends JFrame {

    private JButton newGame,info,ip,joinGame,quit,name;
    private JTextField ipText,nameText;
    private JPanel bodyContainer,container;
    private JLabel welcome;
    private GridBagConstraints constraints;
    private String message;
    private boolean isConnected;
    private boolean hasName;
    private JList<String> list;
    private DefaultListModel model;
    private JScrollPane pane;
    private String lastName;

    private CheckersClient client;
    private ClientListener listener;

    GuiWelcome(CheckersClient client){

        super("Welcome menu");
        this.client=client;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        message = " Chinese Checkers \n Kacper Szatan i Piotr Borowczyk";
        isConnected = false;
        hasName=false;

        createComponents();
        setContainer();
        addComponents();
        addListeners();

        setSize(400, 700);
        setLocation(400,200);

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
        name = new JButton("Set your name: ");
        welcome.setFont(new Font("Comic Sans MS", Font.PLAIN | Font.BOLD, 30));
        newGame = new JButton("New Game");
        joinGame = new JButton("Join Game");
        info = new JButton("Info");
        quit = new JButton("Exit");
        ip = new JButton("Connect and refresh:");
        ipText = new JTextField("127.0.0.1");
        nameText = new JTextField("");
        model = new DefaultListModel();
        list = new JList<>(model);
        pane = new JScrollPane(list);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        bodyContainer.add(name,constraints);
        bodyContainer.add(nameText,constraints);

        constraints.insets= new Insets(25,0,0,0);
        bodyContainer.add(newGame,constraints);
        constraints.insets= new Insets(0,0,0,0);
        bodyContainer.add(joinGame,constraints);
        bodyContainer.add(pane,constraints);

        constraints.insets= new Insets(25,0,0,0);
        bodyContainer.add(info,constraints);
        constraints.insets= new Insets(0,0,0,0);
        bodyContainer.add(quit,constraints);

    }

    private void addListeners(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    listener.quit(0);
                }else{
                    GuiWelcome.this.setVisible(false);
                    GuiWelcome.this.dispose();
                }
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected&&hasName) {
                    checkName();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if(hasName) {
                        client.createGuiSetup();
                    }
                }
            }
        });

        joinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected&&hasName) {
                    checkName();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if(hasName) {
                        listener.sendMessage("join;" + list.getSelectedValue());
                    }
                }
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GuiWelcome.this , message,"Info",JOptionPane.PLAIN_MESSAGE);
            }
        });

        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected) {
                    lastName = nameText.getText();
                    checkName();
                }
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener != null){
                    listener.quit(0);
                }else{
                    GuiWelcome.this.setVisible(false);
                    GuiWelcome.this.dispose();
                }
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
                if(isConnected){
                    refresh();
                }
            }
        });
    }

    protected void showMessage(String message){
        JOptionPane.showMessageDialog(GuiWelcome.this , message,"Message",JOptionPane.PLAIN_MESSAGE);
    }

    protected void setListener(ClientListener listener){
        this.listener=listener;
    }

    protected void setHasName(boolean state){
        this.hasName = state;
        if(!state) {
            showMessage("Your name is already in use, change it!");
        }
    }
    private void refresh(){
        listener.sendMessage("refresh");
    }

    protected void setList(String arguments){
        model.removeAllElements();
        String[] tmp = arguments.split(";");
        for(int i=1;i<tmp.length;i++){
            model.addElement(tmp[i]);
        }
    }
    private void checkName(){
        if(lastName.equals("Anonymous")){
            setHasName(true);
        }else {
            listener.sendMessage("name;" + lastName);
        }
    }

}
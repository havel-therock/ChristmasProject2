package com.checkers.client;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GuiWelcome extends JFrame {

    private JButton newGame,info,ip,joinGame,quit,name,delete;
    private JTextField ipText,nameText;
    private JPanel bodyContainer,container;
    private JLabel welcome;
    private GridBagConstraints constraints;
    private boolean isConnected;
    private boolean hasName;
    private JList<String> list;
    private DefaultListModel model;
    private JScrollPane pane;
    private String lastName;

    private ClientMain client;
    private CheckersClient checkersClient;

    GuiWelcome(ClientMain client){

        super("Welcome menu");
        this.client=client;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        isConnected = false;
        hasName=false;

        createComponents();
        setContainer();
        addComponents();
        addListeners();

        setSize(400, 700);
        setLocation(400,20);

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
        delete = new JButton("Delete Game");
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
        bodyContainer.add(delete,constraints);
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
                if(checkersClient != null){
                    checkersClient.quit(0);
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
                        if(list.getSelectedValue()!=null) {
                            checkersClient.sendMessage("join;" + list.getSelectedValue());
                        }else{
                            showMessage("Please, select a game");
                        }
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected){
                    if(list.getSelectedValue()!=null) {
                        checkersClient.sendMessage("delete;"+list.getSelectedValue());
                    }else{
                        showMessage("Please, select a game");
                    }
                }
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = " Chinese Checkers \n Kacper Szatan i Piotr Borowczyk \n\n Tip: For an anonymous user, set your name to \"anonymous\" ";
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
                if(checkersClient != null){
                    checkersClient.quit(0);
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

    protected void setCheckersClient(CheckersClient checkersClient){
        this.checkersClient=checkersClient;
    }

    protected void setHasName(boolean state){
        this.hasName = state;
        if(!state){
            nameText.setForeground(Color.red.darker());
            showMessage("Your name is already in use, change it!");
        }
    }
    private void refresh(){checkersClient.sendMessage("refresh");
    }

    protected void setList(String arguments){
        model.removeAllElements();
        String[] tmp = arguments.split(";");
        for(int i=1;i<tmp.length;i++){
            model.addElement(tmp[i]);
        }
    }
    private void checkName(){
        if(isStringLegit(lastName)) {
            if (lastName.equals("anonymous")) {
                hasName = true;
            } else {
                checkersClient.sendMessage("name;" + lastName);
            }
        }else{
            hasName = false;
        }
    }
    private boolean isStringLegit(String string){
        if(string.equals("")) {
            showMessage("Your name is empty :/");
            return false;
        }

        String sub = string.substring(0,1);
        if(string.contains(";")||sub.equals(" ")){
            nameText.setForeground(Color.red.darker());
            showMessage("Your name contains ; or starts with a space - change that ;)");
            return false;
        }else{
            nameText.setForeground(Color.green.darker());
            return true;
        }
    }

}
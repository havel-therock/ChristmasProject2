package com.checkers.server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player {
    String idGame;
    boolean ifActive;
    int number; // number of your team (pieces)
    BufferedReader reader;
    PrintWriter writer;

    Player(BufferedReader reader, PrintWriter writer){
        idGame = null;
        ifActive = false;
        number = 0;
        this.reader = reader;
        this.writer = writer;
    }
}

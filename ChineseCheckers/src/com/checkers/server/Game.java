package com.checkers.server;

import com.checkers.client.CheckersClient;

import java.util.ArrayList;

public class Game {
    Board b;
    Rules r;
    String name;
    ArrayList<Runnable> players = new ArrayList<Runnable>();

    Game(String[] arguments, Player player){
        this.name = arguments[1];
        b = new Board();
        r = new Rules();
        players.add(player);

    }

    public String getName() {
        return name;
    }

    protected void addPlayer(Player player){
        players.add(player);
    }

    protected void delete(Player player){
        players.remove(player);
    }

    protected void deletePieces(int number){
        b.deletePlayer(number);

    }

}

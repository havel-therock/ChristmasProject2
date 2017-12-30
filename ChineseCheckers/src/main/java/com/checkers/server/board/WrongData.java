package com.checkers.server.board;

public class WrongData extends Exception {
    public String message;

    WrongData(String message){
        this.message = message;
    }
}

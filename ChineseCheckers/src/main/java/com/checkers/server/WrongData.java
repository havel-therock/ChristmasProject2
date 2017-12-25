package com.checkers.server;

public class WrongData extends Exception {
    String message;

    WrongData(String message){
        this.message = message;
    }
}

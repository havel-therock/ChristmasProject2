package com.checkers.server;

public interface User {

    String name = "anonymous";

    void writeToPlayer(String message);

    void setIfActive(boolean state);

    void setNumber(int number);

    int getCornerNumber();
    int getNumber();
}

package com.checkers.server.board;

import java.util.ArrayList;

class Field {
    Field(int ID, int value){
        this.ID = ID;
        this.value = value;
        Neighbours = new ArrayList<Field>();
    }
    private int ID;
    private int value;
    ArrayList<Field> Neighbours;

    public int getID() {
        return ID;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

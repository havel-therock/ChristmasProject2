package com.checkers.server.board;

import java.util.ArrayList;

public class Field {
    Field(int ID, int value){
        this.ID = ID;
        this.value = value;
        targetValue = Board.NOT_PLAYABLE_FIELD;
        Neighbours = new ArrayList<Field>();
        directions = new int[6];
        for(int i = 0; i < 6; i++){
            directions[i] = 0;
        }
    }
    private int ID;
    private int value;
    private int targetValue;
    public ArrayList<Field> Neighbours;
    public int[] directions;

    public int getID() {
        return ID;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setTargetValue(int targetValue){
        this.targetValue = targetValue;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setPlayer(int value) {
        this.value = value;
        targetValue = (value + 2) % 6 + 1;
    }

    public int getNeighbourDirection(int ID){
        for(int i = 0; i < directions.length; i++){
            if(directions[i] == ID)
                return i;
        }
        return -1;
    }


    //get target value i get target beda uzywane do sprawdzania czy ktos wygral.
    // sprawdzanie po wszystkich field dla ktorych get target  == 1  to jesli dla wszytskich get target == 1
    // get value == 1 to gracz pierwszy wygral i tak dalej po wszystkich graczach
}

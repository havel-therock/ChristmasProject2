package com.checkers.server;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Rules {
    ArrayList<Field> graph;

    boolean jumpOver = false;
    boolean bigHops = false;
    boolean multiJumpsOver = false;
    boolean jumpOverGivesPenalty = false;
    boolean step = true;
    boolean
    //...

    //Rules(ArrayList<Field> graph){
     //   this.graph = graph;
//
 //   }

    boolean checkMove(String move){
        if(step(move) == false)
            return false;
        if(jumpOver(move) == false)
            return false;
        if(bigHops(move) == false)
            return false;
        //...

        return true;
    }

    boolean step(String move){
        if(this.step == false){
            return true;
        }else{

            return true; // or false zalezy co wyjdzie z linijek powyzej
        }
    }

    boolean jumpOver(String move){
        if(this.jumpOver == false){
            return true;
        }else{
            //sprawdzanie poprawnosci przeskokow
            return true; // or false zalezy co wyjdzie z linijek powyzej
        }
    }

    boolean bigHops(String move){
        if(this.bigHops == false){
            return true;
        }else{
            //sprawdzanie poprawnosci przeskokow
            return true; // or false zalezy co wyjdzie z linijek powyzej
        }
    }

    private int getID(int row, int col){
        int ID = 1;
        if(row > 1) {
            ID = sumFields(row);
        }
        int colCounter;
        for(colCounter = 0; colCounter <= col; colCounter++){
        //    if(tempBoard[row][colCounter] != NOT_PLAYABLE_FIELD){
                ID++;
         //   }
        }
        return ID;
    }
}

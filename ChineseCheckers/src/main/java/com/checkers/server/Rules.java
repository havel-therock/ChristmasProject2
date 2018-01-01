package com.checkers.server;

public class Rules {
    boolean jumpOver = false;
    boolean bigHops = false;
    boolean multiJumpsOver = false;
    boolean jumpOverGivesPenalty = false;
    boolean step = true;
    //...

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
}

package com.checkers.server;

public class Board {
    int[][] board;

    Board(String[] arguments) throws WrongData{
        if(validateArguments(arguments)){

        }else{
            throw new WrongData();
            //print message to player smth wrong with arguments of the board
        }
    }

    // arguments[1] amount of players
    // arguments[2] amount of pieces per player
    // arguments[3] amount of corners
    // arguments[4] width of corner's base
    boolean validateArguments(String[] arguments){
        int players = 0;
        int pieces = 0;
        int corners = 0;
        int cornerBase = 0;
        try{
            players = Integer.parseInt(arguments[2]);
            pieces = Integer.parseInt(arguments[3]);
            corners = Integer.parseInt(arguments[4]);
            cornerBase  = Integer.parseInt(arguments[5]);
        }catch(NumberFormatException ex){
            //ex.printStackTrace();
            return false;
            //message to player you are not giving numbers
            //return false; ???
        }
        if(reverseAdding(cornerBase) < pieces){
            // message?
            return false;
        }

        return true;
    }

    int reverseAdding(int i){
            if(i<1)
                return 0;
            return i+reverseAdding(i-1);
    }

    
    void buildCorner(int baseWidth, int piecesAmount){

    }

    void createTriangle(){

    }

    void createReverseTriangle(){

    }


    void updateBoard(String move){

    }

    String getBoard(){
        return "board";
    }



    void deletePlayer(int number){

    }

    void executeMove(String move){

    }
}

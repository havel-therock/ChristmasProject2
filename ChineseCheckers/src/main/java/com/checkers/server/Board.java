package com.checkers.server;

public class Board {
    int[][] board;
    int players = 0;
    int pieces = 0;
    int corners = 0;
    int cornerWidth = 0;
    int length;

    Board(String[] arguments) throws WrongData{
        String message = validateArguments(arguments);
        if (!"ArgumentsClear".equals(message)) {
            throw new WrongData(message);
            //print message to player smth wrong with arguments of the board
        } else {
            createBoard();
            //fillPlayers();
            return;
        }
    }

    private void createBoard(){
        int i,j,tmp,counter;
        length=cornerWidth*4+3;
        board = new int[length][length];

        for(i=0;i<length;i++){
            for(j=0;j<length;j++){
                board[i][j]=-1;
            }
        }

        // filling the board array



        for(i=0;i<length;i++){
            for(j=0;j<length;j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
    }



    /*
        arguments[2] amount of players
        arguments[3] amount of pieces per player
        arguments[4] amount of corners
        arguments[5] width of corner's base
    */
    String validateArguments(String[] arguments){
        try{
            players = Integer.parseInt(arguments[2]);
            pieces = Integer.parseInt(arguments[3]);
            corners = Integer.parseInt(arguments[4]);
            cornerWidth  = Integer.parseInt(arguments[5]);
        }catch(NumberFormatException ex){
            //ex.printStackTrace();
            return "Please enter some INTEGERS!!!";
            //message to player you are not giving numbers
            //return false; ???
        }
        if(reverseAdding(cornerWidth) < pieces){
            // message?
            return "Too many pieces for corner size";
        }

        return "ArgumentsClear";
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
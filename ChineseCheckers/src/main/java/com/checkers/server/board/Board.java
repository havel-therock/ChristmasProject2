package com.checkers.server.board;

public class Board {
    int[] fieldsPerRow;
    int[][] tempBoard;
    int[][] graph;
    int players;
    int pieces;
    int corners;
    int cornerWidth;
    //int length;

    public Board(String[] arguments) throws WrongData {
        String message = validateArguments(arguments);
        if (!"ArgumentsClear".equals(message)) {
            throw new WrongData(message);
            //print message to player smth wrong with arguments of the board
        } else {
            setBoardFields(arguments);
            displayboard(fieldsPerRow);
            createTempBoard();
           // createBoard();
            //fillPlayers();
        }
    }

    private int sumFields(){
        int sum = 0;
        for(int i = 0; i < fieldsPerRow.length; i++){
            sum = sum + fieldsPerRow[i];
        }
        return sum;
    }

    private void createTempBoard(){
        this.tempBoard = new int[(this.cornerWidth*4 + 1) + 2][((this.cornerWidth*3 + 1)*2 - 1) + 2];  //int[rows][columns]
        prefillTempBoard((this.cornerWidth*4 + 1) + 2, ((this.cornerWidth*3 + 1)*2 - 1) + 2);
        fillTempBoard((this.cornerWidth*4 + 1) + 2, ((this.cornerWidth*3 + 1)*2 - 1) + 2);
        displayboard2(tempBoard,(this.cornerWidth*4 + 1) + 2, ((this.cornerWidth*3 + 1)*2 - 1) + 2);
    }

    private void fillTempBoard(int height, int length){
        for(int i = 1; i < height - 1; i++){
            int fields = fieldsPerRow[i - 1];
            int verticalCenter = length/2 + 1;
            int start = verticalCenter - fields;
            for(int j=0;j<fields;j++){
                this.tempBoard[i][start] = 0;
                start = start + 2;
            }
        }
    }



    public void displayboard(int[] board){              //  temporary to display board
        for(int i = 0; i < board.length; i++)           //  for development usage
            System.out.println(board[i]);               //  need to be deleted in final version
    }

    public void displayboard2(int[][] board, int height, int length){            //  temporary to display board
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++)                                    //  for development usage
                System.out.print(board[i][j]);                                       //  need to be deleted in final version
            System.out.print("\n");
        }
    }


    private void prefillTempBoard(int height, int length){
        for(int i=0;i<height;i++){
            for(int j=0;j<length;j++){
                this.tempBoard[i][j]=-1;
            }
        }
    }

    private void setBoardFields(String[] arguments){
        players = Integer.parseInt(arguments[2]);
        pieces = Integer.parseInt(arguments[3]);
        corners = Integer.parseInt(arguments[4]);
        cornerWidth = Integer.parseInt(arguments[5]);
        setFieldsPerRow();
    }

    private void setFieldsPerRow(){
        int height = cornerWidth*4 + 1;
        this.fieldsPerRow = new int[height];
        int fields = 0;
        for(int i = 0; i < height; i++){
            if(fields < cornerWidth){
                fields++;
                this.fieldsPerRow[i] = fields;
            }else{
                fields = fields*3 + 1;
                while(i < height/2 + 1){
                    fieldsPerRow[i] = fields;
                    fields--;
                    i++;
                }
                for(int j = i-2; j >= 0; j--){
                    fieldsPerRow[i] = fieldsPerRow[j];
                    i++;
                }
            }
        }

    }


    /*
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



    public void deletePlayer(int number){

    }

    public void executeMove(String move){

    }
}


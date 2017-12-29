package com.checkers.server.board;

import java.util.ArrayList;

//    1
// 6 0 0 2
//  0 0 0
// 5 0 0 3
//    4
//Board and it's corners(players' numbers)

public class Board {
    int[] fieldsPerRow;
    int[][] tempBoard;
    int tempBoardHeigh;
    int tempBoardLength;
    ArrayList<Field> graph;
    int players;
    int pieces;
    int corners;
    int cornerWidth;
    final static int BOARD_FIELD = 0;
    final static int NOT_PLAYABLE_FIELD = -1;
    final int PLAYER_ONE = 1;
    final int PLAYER_TWO = 2;
    final int PLAYER_THREE = 3;
    final int PLAYER_FOUR = 4;
    final int PLAYER_FIVE = 5;
    final int PLAYER_SIX = 6;

    public Board(String[] arguments) throws WrongData {
        String message = validateArguments(arguments);
        if ("ArgumentsClear".equals(message)) {
            setBoardFields(arguments);
            fillPlayers();
        } else {
            throw new WrongData(message);
        }
    }

    private void setBoardFields(String[] arguments){
        players = Integer.parseInt(arguments[2]);
        pieces = Integer.parseInt(arguments[3]);
        corners = Integer.parseInt(arguments[4]);
        cornerWidth = Integer.parseInt(arguments[5]);
        setFieldsPerRow();
        createTempBoard();
        //displayboard2(tempBoard, tempBoardHeigh, tempBoardLength);
        createGraph();
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

    private void createTempBoard(){
        tempBoardHeigh = (cornerWidth*4 + 1) + 2;
        tempBoardLength = ((cornerWidth*3 + 1)*2 - 1) + 2;
        tempBoard = new int[tempBoardHeigh][tempBoardLength];  //int[rows][columns]
        prefillTempBoard(tempBoardHeigh, tempBoardLength);
        fillTempBoard(tempBoardHeigh, tempBoardLength);
    }

    private void prefillTempBoard(int height, int length){
        for(int i=0;i<height;i++){
            for(int j=0;j<length;j++){
                this.tempBoard[i][j]= NOT_PLAYABLE_FIELD;
            }
        }
    }

    private void fillTempBoard(int height, int length){
        for(int i = 1; i < height - 1; i++){
            int fields = fieldsPerRow[i - 1];
            int verticalCenter = length/2 + 1;
            int start = verticalCenter - fields;
            for(int j=0;j<fields;j++){
                this.tempBoard[i][start] = BOARD_FIELD;
                start = start + 2;
            }


        }
    }

    private void createGraph(){
        //prefill arraylist
        graph = new ArrayList<Field>();
        for(int i = 1; i <= sumFields(fieldsPerRow.length); i++){
            graph.add(new Field(i,BOARD_FIELD));
        }

        //pick one field
        for(int i = 1; i < tempBoardHeigh - 1; i++){
            for(int j = 1; j < tempBoardLength - 1; j++){
                if(tempBoard[i][j] == BOARD_FIELD){
                    int ID = getIDFromTempBoard(i,j);
                    int neighbourID;
                    Field field = null, neighbour = null;
                    for(int a = 0; a < graph.size(); a++){
                        if(graph.get(a).getID() == ID){
                            field = graph.get(a);
                            break;
                        }
                    }
                    //set relations(neighbourhood) for previously picked field
                    //dev//body of the IFs' should be in methods
                    if(tempBoard[i - 1][j - 1] == BOARD_FIELD){
                        neighbourID = getIDFromTempBoard(i - 1, j - 1);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        field.Neighbours.add(neighbour);
                    }
                    if(tempBoard[i - 1][j + 1] == BOARD_FIELD){
                        neighbourID = getIDFromTempBoard(i - 1, j + 1);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        field.Neighbours.add(neighbour);
                    }
                    if(j-2 >= 0)
                        if(tempBoard[i][j - 2] == BOARD_FIELD){
                            neighbourID = getIDFromTempBoard(i, j - 2);
                            for(int a = 0; a < graph.size(); a++){
                                if(graph.get(a).getID() == neighbourID){
                                    neighbour = graph.get(a);
                                    break;
                                }
                            }
                            field.Neighbours.add(neighbour);
                        }
                    if(j+2 < tempBoardLength)
                        if(tempBoard[i][j + 2] == BOARD_FIELD){
                            neighbourID = getIDFromTempBoard(i, j + 2);
                            for(int a = 0; a < graph.size(); a++){
                                if(graph.get(a).getID() == neighbourID){
                                    neighbour = graph.get(a);
                                    break;
                                }
                            }
                            field.Neighbours.add(neighbour);
                        }
                    if(tempBoard[i + 1][j - 1] == BOARD_FIELD){
                        neighbourID = getIDFromTempBoard(i + 1, j - 1);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        field.Neighbours.add(neighbour);
                    }
                    if(tempBoard[i + 1][j + 1] == BOARD_FIELD){
                        neighbourID = getIDFromTempBoard(i + 1, j + 1);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        field.Neighbours.add(neighbour);
                    }
                }
            }
        }
    }

    private int sumFields(int rowsAmount){
        int sum = 0;
        for(int i = 0; i < rowsAmount; i++){
            sum = sum + fieldsPerRow[i];
        }
        return sum;
    }

    private int getIDFromTempBoard(int row, int col){
        int ID = 0;
        if(row > 1) {
            ID = sumFields(row - 1);
        }
        int colCounter;
        for(colCounter = 0; colCounter < col; colCounter++){
            if(tempBoard[row][colCounter] != NOT_PLAYABLE_FIELD){
                ID++;
            }
        }
        ID++;
        return ID;
    }

    private void fillPlayers() {
        switch (players){
            case 2:
                fillFirst();
                fillFourth();
                break;
            case 3:
                //fillFirst();//set target osobno
                break;
            case 4:
                break;
            case 6:
                break;
            default:
                break;
        }
    }

    private void fillFirst(){
        for(int i = 1; i <= sumFields(cornerWidth); i++){
            for(int j = 0; j < graph.size(); j++){
                if(graph.get(j).getID() == i){
                    graph.get(j).setPlayer(PLAYER_ONE);
                    break;
                }
            }
        }
    }

    private void fillSecond(){

    }

    private void fillFourth(){
        for(int i = sumFields(fieldsPerRow.length); i > sumFields(fieldsPerRow.length) - sumFields(cornerWidth); i--){
            for(int j = 0; j < graph.size(); j++){
                if(graph.get(j).getID() == i){
                    graph.get(j).setPlayer(PLAYER_FOUR);
                    break;
                }
            }
        }
    }

    private void fillSixth(){
        int startID = sumFields(cornerWidth) + 1;
        for(int i = cornerWidth; i > 0; i--){
            for(int j = cornerWidth; j > 0; j--){

            }
        }
    }



    public void displayboard(int[] board){              //  temporary to display board
        for(int i = 0; i < board.length; i++)           //  for development usage
            System.out.println(board[i]);               //  need to be deleted in final version
    }

    public void displayboard2(int[][] board, int height, int length){    //  temporary to display board
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++)                             //  for development usage
                System.out.print(board[i][j]);                           //  need to be deleted in final version
            System.out.print("\n");
        }
    }

    public void dis(){
        for(int i = 0; i < graph.size(); i++){
            System.out.print(graph.get(i).getValue());
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
            return "You put some characters in fields where only numbers are allowed";
        }
        if(players < 2 || 6 < players){
            return "Field players must be a number between 2 and 6";
        }
        if(reverseAdding(cornerWidth) < pieces){
            return "Too many pieces for corner size";
        }
        if(pieces < 1){
            return "You can't play without pieces";
        }
        if(cornerWidth < 1){
            return "Width of the corner should be at least 1";
        }
        return "ArgumentsClear";
    }

    int reverseAdding(int i){
        if(i<1)
            return 0;
        return i+reverseAdding(i-1);
    }

/*
    void buildCorner(int baseWidth, int piecesAmount){          //probably to delete

    }

    void createTriangle(){

    }

    void createReverseTriangle(){

    }
*/

    void updateBoard(String move){

    }

    String getBoard(){
        return "board";
    }



    public void deletePlayer(int number){

    }

    public void executeMove(String move){
        //if move correct then swap values
        //swapValues();
    }

    public void swapValues(Field field1, Field field2){
        int temp = field1.getValue();
        field1.setValue(field2.getValue());
        field2.setValue(temp);
    }
}


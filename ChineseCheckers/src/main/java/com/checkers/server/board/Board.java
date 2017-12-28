package com.checkers.server.board;

import java.util.ArrayList;

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

    public Board(String[] arguments) throws WrongData {
        String message = validateArguments(arguments);
        if ("ArgumentsClear".equals(message)) {
            setBoardFields(arguments);
            //fillPlayers();
        } else {
            throw new WrongData(message);
            //print message to player smth wrong with arguments of the board
        }
    }

    private void setBoardFields(String[] arguments){
        players = Integer.parseInt(arguments[2]);
        pieces = Integer.parseInt(arguments[3]);
        corners = Integer.parseInt(arguments[4]);
        cornerWidth = Integer.parseInt(arguments[5]);
        setFieldsPerRow();
        createTempBoard();
        displayboard2(tempBoard, tempBoardHeigh, tempBoardLength);
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
                this.tempBoard[i][j]= 1;
            }
        }
    }

    private void fillTempBoard(int height, int length){
        int counter = 0;
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

    // create array of Fields
    // with value 0 w/o relations yet

    private void createGraph(){
        graph = new ArrayList<Field>();
        for(int i = 1; i <= sumFields(fieldsPerRow.length); i++){
            graph.add(new Field(i,0));
        }
        System.out.println(graph.size());
        for(int i = 1; i < tempBoardHeigh - 1; i++){
            for(int j = 1; j < tempBoardLength - 1; j++){
                if(tempBoard[i][j] == 0){
                    int ID = getIDFromTempBoard(i,j);
                    System.out.println("current ID: " + ID);
                    int neighbourID;
                    Field field = null, neighbour = null;
                    for(int a = 0; a < graph.size(); a++){
                        if(graph.get(a).getID() == ID){
                            field = graph.get(a);
                            break;
                        }
                    }
                    //System.out.println(field.getID());

                    if(tempBoard[i - 1][j - 1] == 0){
                        neighbourID = getIDFromTempBoard(i - 1, j - 1);
                        System.out.println("current neighbourID: " + neighbourID);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        //System.out.println("current neigbourIndex: " + neighbourIndex);
                        field.Neighbours.add(neighbour);
                    }
                    if(tempBoard[i - 1][j + 1] == 0){
                        neighbourID = getIDFromTempBoard(i - 1, j + 1);
                        System.out.println("current neighbourID: " + neighbourID);

                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        //System.out.println("current index: " + index);
                        //System.out.println("current neigbourIndex: " + neighbourIndex);
                        field.Neighbours.add(neighbour);
                    }
                    if(j-2 >= 0)
                        if(tempBoard[i][j - 2] == 0){
                            neighbourID = getIDFromTempBoard(i, j - 2);
                            System.out.println("current neighbourID: " + neighbourID);
                            for(int a = 0; a < graph.size(); a++){
                                if(graph.get(a).getID() == neighbourID){
                                    neighbour = graph.get(a);
                                    break;
                                }
                            }
                            //System.out.println("current index: " + index);
                            //System.out.println("current neigbourIndex: " + neighbourIndex);
                            field.Neighbours.add(neighbour);
                        }
                    if(j+2 < tempBoardLength)
                        if(tempBoard[i][j + 2] == 0){
                            neighbourID = getIDFromTempBoard(i, j + 2);
                            System.out.println("current neighbourID: " + neighbourID);
                            for(int a = 0; a < graph.size(); a++){
                                if(graph.get(a).getID() == neighbourID){
                                    neighbour = graph.get(a);
                                    break;
                                }
                            }
                            //System.out.println("current index: " + index);
                            //System.out.println("current neigbourIndex: " + neighbourIndex);
                            field.Neighbours.add(neighbour);
                        }
                    if(tempBoard[i + 1][j - 1] == 0){
                        neighbourID = getIDFromTempBoard(i + 1, j - 1);
                        System.out.println("current neighbourID: " + neighbourID);

                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        //System.out.println("current index: " + index);
                        //System.out.println("current neigbourIndex: " + neighbourIndex);
                        field.Neighbours.add(neighbour);
                    }
                    if(tempBoard[i + 1][j + 1] == 0){
                        neighbourID = getIDFromTempBoard(i + 1, j + 1);
                        System.out.println("current neighbourID: " + neighbourID);
                        for(int a = 0; a < graph.size(); a++){
                            if(graph.get(a).getID() == neighbourID){
                                neighbour = graph.get(a);
                                break;
                            }
                        }
                        //System.out.println("current index: " + index);
                        //System.out.println("current neigbourIndex: " + neighbourIndex);
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
            if(tempBoard[row][colCounter] != 1){
                ID++;
            }
        }
        ID++;
        return ID;
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

    }
}


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
    int tempBoardHeight;
    int tempBoardLength;
    ArrayList<Field> graph;
    int players;
    int pieces;
    int corners;
    int cornerWidth;
    public final static int BOARD_FIELD = 0;
    public final static int NOT_PLAYABLE_FIELD = -1;
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
            //dis();
            displayboard2(tempBoard, tempBoardHeight, tempBoardLength);
            System.out.println(getBoard());
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
        tempBoardHeight = (cornerWidth*4 + 1) + 2;
        tempBoardLength = ((cornerWidth*3 + 1)*2 - 1) + 2;
        tempBoard = new int[tempBoardHeight][tempBoardLength];  //int[rows][columns]
        prefillTempBoard(tempBoardHeight, tempBoardLength);
        fillTempBoard(tempBoardHeight, tempBoardLength);
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
        for(int i = 1; i < tempBoardHeight - 1; i++){
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
//ograniczenie do pieces dodac jakos tak zeby w kazdym rogu byly pionki ulozone rowno nawet jak jest ich mniej
    private void fillPlayers() {
        switch (players){
            case 2:
                fillFirst();
                fillFourth();
                break;
            case 3:
                fillFirst();
                fillThird();
                fillFifth();
                //fillFirst();
                // FillAll() jak dla przypadku trzeciego ale skasuj potem trzech graczy wtedy
                //target zostanie zachowany
                //\/\/\/belows to delete / need to create them to set target for players
                fillSecond();
                fillFourth();
                fillSixth();
                deletePlayer(2);
                deletePlayer(4);
                deletePlayer(6);
                break;
            case 4:
                fillSecond();
                fillThird();
                fillFifth();
                fillSixth();
                break;
            case 6:
                fillFirst();
                fillSecond();
                fillThird();
                fillFourth();
                fillFifth();
                fillSixth();
                break;
            default:
                break;
        }
    }

    private void fillFirst(){
        for(int i = 1; i <= pieces; i++){
            for(int j = 0; j < graph.size(); j++){
                if(graph.get(j).getID() == i){
                    graph.get(j).setPlayer(PLAYER_ONE);
                    break;
                }
            }
        }
    }

    private void fillSecond(){
        int startID = sumFields(cornerWidth) + fieldsPerRow[cornerWidth];
        int seekID = startID;
        Field levelField, checkerField = null, setterField;
        for(int a = 0; a < graph.size(); a++){
            if(graph.get(a).getID() == seekID){
                setterField = graph.get(a);
                checkerField = setterField;
                setterField.setPlayer(PLAYER_TWO);
                break;
            }
        }

        int counter = 1;
        levelField = checkerField.Neighbours.get(0);
        int limitSet = 1;
        while(counter < pieces){
            //next level of pieces
            for(int i = 0; i < checkerField.Neighbours.size(); i++){
                if(checkerField.Neighbours.get(i).getID() < levelField.getID()){
                    levelField = checkerField.Neighbours.get(i);
                }
            }
            levelField.setPlayer(PLAYER_TWO);
            counter++;
            setterField = levelField; // prepare to use setterfield for putting pieces
            checkerField = setterField;
            // fill the line of pieces
            boolean run = true;
            limitSet++;
            int limit = limitSet;
            while(counter < pieces && run && limit > 0){
                run = false;
                int size = checkerField.Neighbours.size();
                for(int i = 0; i < size; i++){
                    if(checkerField.Neighbours.get(i).getID() > setterField.getID() && checkerField.Neighbours.get(i).getValue() == 0){
                        setterField = checkerField.Neighbours.get(i);
                        run = true;

                    }
                }
                if(run == true){
                    setterField.setPlayer(PLAYER_TWO);
                    checkerField = setterField;
                    counter++;
                    limit--;
                }
            }
            setterField = levelField; // prepare to drop down a level of pieces
            checkerField = setterField;
        }


    }

    private void fillThird(){
        int startID = sumFields(fieldsPerRow.length) - sumFields(cornerWidth);
        int seekID = startID;
        Field levelField, checkerField = null, setterField;
        for(int a = 0; a < graph.size(); a++){
            if(graph.get(a).getID() == seekID){
                setterField = graph.get(a);
                checkerField = setterField;
                setterField.setPlayer(PLAYER_THREE);
                break;
            }
        }

        int counter = 1;
        levelField = checkerField.Neighbours.get(0);
        int limitSet = 1;
        while(counter < pieces){
            //next level of pieces
            for(int i = 0; i < checkerField.Neighbours.size(); i++){
                if(checkerField.Neighbours.get(i).getID() < levelField.getID()){
                    levelField = checkerField.Neighbours.get(i);
                }
            }
            levelField.setPlayer(PLAYER_THREE);
            counter++;
            setterField = levelField; // prepare to use setterfield for putting pieces
            checkerField = setterField;
            // fill the line of pieces
            boolean run = true;
            limitSet++;
            int limit = limitSet;
            while(counter < pieces && run && limit > 0){
                run = false;
                int size = checkerField.Neighbours.size();
                for(int i = 0; i < size; i++){
                    if(checkerField.Neighbours.get(i).getID() > setterField.getID() && checkerField.Neighbours.get(i).getValue() == 0){
                        setterField = checkerField.Neighbours.get(i);
                        run = true;

                    }
                }
                if(run == true){
                    setterField.setPlayer(PLAYER_THREE);
                    checkerField = setterField;
                    counter++;
                    limit--;
                }
            }
            setterField = levelField; // prepare to drop down a level of pieces
            checkerField = setterField;
        }
    }

    private void fillFourth(){
        for(int i = sumFields(fieldsPerRow.length); i > sumFields(fieldsPerRow.length) - pieces; i--){
            for(int j = 0; j < graph.size(); j++){
                if(graph.get(j).getID() == i){
                    graph.get(j).setPlayer(PLAYER_FOUR);
                    break;
                }
            }
        }
    }

    private void fillFifth(){
        int startID = sumFields(fieldsPerRow.length) - sumFields(cornerWidth) - fieldsPerRow[cornerWidth] + 1;
        int seekID = startID;
        Field levelField, checkerField = null, setterField;
        for(int a = 0; a < graph.size(); a++){
            if(graph.get(a).getID() == seekID){
                setterField = graph.get(a);
                checkerField = setterField;
                setterField.setPlayer(PLAYER_FIVE);
                break;
            }
        }

        int counter = 1;
        levelField = checkerField.Neighbours.get(0);
        int limitSet = 1;
        while(counter < pieces){
            //next level of pieces
            for(int i = 0; i < checkerField.Neighbours.size(); i++){
                if(checkerField.Neighbours.get(i).getID() > levelField.getID()){
                    levelField = checkerField.Neighbours.get(i);
                }
            }
            levelField.setPlayer(PLAYER_FIVE);
            counter++;
            setterField = levelField; // prepare to use setterfield for putting pieces
            checkerField = setterField;
            // fill the line of pieces
            boolean run = true;
            limitSet++;
            int limit = limitSet;
            while(counter < pieces && run && limit > 0){
                run = false;
                int size = checkerField.Neighbours.size();
                for(int i = 0; i < size; i++){
                    if(checkerField.Neighbours.get(i).getID() < setterField.getID() && checkerField.Neighbours.get(i).getValue() == 0){
                        setterField = checkerField.Neighbours.get(i);
                        run = true;

                    }
                }
                if(run == true){
                    setterField.setPlayer(PLAYER_FIVE);
                    checkerField = setterField;
                    counter++;
                    limit--;
                }
            }
            setterField = levelField; // prepare to drop down a level of pieces
            checkerField = setterField;
        }

    }

    private void fillSixth(){
        int startID = sumFields(cornerWidth) + 1;
        int seekID = startID;
        Field levelField, checkerField = null, setterField;
        for(int a = 0; a < graph.size(); a++){
            if(graph.get(a).getID() == seekID){
                setterField = graph.get(a);
                checkerField = setterField;
                setterField.setPlayer(PLAYER_SIX);
                break;
            }
        }

        int counter = 1;
        levelField = checkerField.Neighbours.get(0);
        while(counter < pieces){
            //next level of pieces
            for(int i = 0; i < checkerField.Neighbours.size(); i++){
                if(checkerField.Neighbours.get(i).getID() > levelField.getID()){
                    levelField = checkerField.Neighbours.get(i);
                }
            }
            levelField.setPlayer(PLAYER_SIX);
            counter++;
            setterField = levelField; // prepare to use setterfield for putting pieces
            checkerField = setterField;
            // fill the line of pieces
            boolean run = true;
            while(counter < pieces && run){
                run = false;
                int size = checkerField.Neighbours.size();
                for(int i = 0; i < size; i++){
                    if(checkerField.Neighbours.get(i).getID() < setterField.getID() && checkerField.Neighbours.get(i).getValue() == 0){
                        setterField = checkerField.Neighbours.get(i);
                        setterField.setPlayer(PLAYER_SIX);
                        checkerField = setterField;
                        counter++;
                        run = true;
                        break;
                    }
                }
            }
            setterField = levelField; // prepare to drop down a level of pieces
            checkerField = setterField;
        }
    }



    public void displayboard(int[] board){              //  temporary to display board
        for(int i = 0; i < board.length; i++)           //  for development usage
            System.out.println(board[i]);               //  need to be deleted in final version
    }

    public static void displayboard2(int[][] board, int height, int length){    //  temporary to display board
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++)//  for development usage
                if(board[i][j] == NOT_PLAYABLE_FIELD)
                    System.out.print(" ");
                else
                    System.out.print(board[i][j]);                           //  need to be deleted in final version
            System.out.print("\n");
        }
    }

    public void dis(){
        int counterID = 1;
        for(int i = 0; i < fieldsPerRow.length; i++){
            for(int j = 0; j < fieldsPerRow[i]; j++){
                for(int a = 0; a < graph.size(); a++){
                    if(graph.get(a).getID() == counterID){
                        System.out.print(+ graph.get(a).getValue() + " ");
                        counterID++;
                        break;
                    }
                }
            }
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
            return "You put some characters in fields where only numbers are allowed";
        }
        if(players < 2 || 6 < players){
            return "Field players must be a number between 2 and 6";
        }
        if(players == 5){
            return "You can't play with 5 people";
        }
        if(cornerWidth < 1){
            return "Width of the corner should be at least 1";
        }
        if(pieces < 1){
            return "You can't play without pieces";
        }
        if(reverseAdding(cornerWidth) < pieces){
            return "Too many pieces for corner size";
        }

        return "ArgumentsClear";
    }

    int reverseAdding(int i){
        if(i<1)
            return 0;
        return i+reverseAdding(i-1);
    }

    void refreshBoard(String move){
        String board = "boardMove;";
    }

    public String getBoard(){
        String board = "boardSetUp;";
        int ID = 1;
        for(int i = 0; i < fieldsPerRow.length; i++){
            for(int j = 0; j < fieldsPerRow[i]; j++){
                for(int a = 0; a < graph.size(); a++){
                    if(graph.get(a).getID() == ID){
                        board = board + graph.get(a).getValue() + ",";
                        ID++;
                        break;
                    }
                }
            }
            board = board + ";";
        }
        return board;
    }



    public void deletePlayer(int number){
        for(int i = 0; i < graph.size(); i++){
            if(graph.get(i).getValue() == number){
                graph.get(i).setValue(BOARD_FIELD);
            }
        }

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


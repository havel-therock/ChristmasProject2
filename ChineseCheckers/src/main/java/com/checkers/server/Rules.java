package com.checkers.server;

import com.checkers.server.board.Board;
import com.checkers.server.board.Field;
import java.util.ArrayList;

public class Rules {
    ArrayList<Field> graph;
    int[] fieldsPerRow;
    int[][] tempBoard;

    boolean moveOnlyToEmptyField;
    boolean stepSizeOneOnly;
    boolean jumpOverOneOnly;

    boolean multiJumpsOver;
    boolean canNotEscapeTargetCorner;
    //boolean jumpOverGivesPenalty;
    //boolean bigHops;

    boolean moveOnlyYourPieces;
    //...

    private Rules(final Builder builder){
        this.graph = builder.graph;
        this.fieldsPerRow = builder.fieldsPerRow;
        this.tempBoard = builder.tempBoard;

        this.moveOnlyToEmptyField = builder.moveOnlyToEmptyField;

        this.stepSizeOneOnly = builder.stepSizeOneOnly;
        this.jumpOverOneOnly = builder.jumpOverOneOnly;
        this.multiJumpsOver = builder.multiJumpsOver;
        this.canNotEscapeTargetCorner = builder.canNotEscapeTargetCorner;
        this.moveOnlyYourPieces = builder.moveOnlyYourPieces;
        //...
    }

    int checkIfWon(){
        for(int i = 0; i < graph.size(); i++){

        }
    }

    boolean checkMove(String move){
        if(moveOnlyYourPieces(move) == false)
            return false;
        if(moveOnlyToEmptyField(move) == false)
            return false;
        if(stepSizeOneOnly(move) == false)
            return false;
        if(jumpOverOneOnly(move) == false)
            return false;
        if(multiJumpsOver(move) == false)
            return false;
        if(canNotEscapeTargetCorner(move) == false)
            return false;

        //...

        return true;
    }

    boolean moveOnlyYourPieces(String move){
        if(this.moveOnlyYourPieces == false) {
            return true;
        }else{
            String[] args = move.split(";");
            try{
                Field field1 = null;
                int row1 = Integer.parseInt(args[1]);
                int col1 = Integer.parseInt(args[2]);
                int field1ID = getID(row1,col1);
                for(int i = 0; i < graph.size(); i++){
                    if(graph.get(i).getID() == field1ID){
                        field1 = graph.get(i);
                    }
                }
                int playerNumber = Integer.parseInt(args[5]);
                if(playerNumber == field1.getValue()) {
                    return true;
                }else {
                    return false;
                }
            }catch (NumberFormatException ex){
                System.out.println("Wrong data");
                return false;
            }
        }
    }

    boolean moveOnlyToEmptyField(String move){
        if(this.moveOnlyToEmptyField == false) {
            return true;
        }else{
            String[] args = move.split(";");

            try{
                Field field1 = null, field2 = null;
                int field1ID, field2ID;
                int row1 = Integer.parseInt(args[1]);
                int col1 = Integer.parseInt(args[2]);
                int row2 = Integer.parseInt(args[3]);
                int col2 = Integer.parseInt(args[4]);
                field1ID = getID(row1,col1);
                field2ID = getID(row2,col2);
                for(int i = 0; i < graph.size(); i++){
                    if(graph.get(i).getID() == field1ID){
                        field1 = graph.get(i);

                    } else if(graph.get(i).getID() == field2ID) {
                        field2 = graph.get(i);
                    }
                }
                if(field2.getValue() == Board.BOARD_FIELD)
                    return true;
                else
                    return false;

            }catch(NumberFormatException ex){
                System.out.println("wrong data");
                return false;
            }
        }
    }

    boolean multiJumpsOver(String move){
        if(this.multiJumpsOver == false){
            return true;
        }else{
         return false;
        }
    }

    boolean canNotEscapeTargetCorner(String move){
        if(this.canNotEscapeTargetCorner == false){
            return true;
        }else{
            String[] args = move.split(";");
            try{
                Field field1 = null, field2 = null;
                int field1ID, field2ID;
                int row1 = Integer.parseInt(args[1]);
                int col1 = Integer.parseInt(args[2]);
                int row2 = Integer.parseInt(args[3]);
                int col2 = Integer.parseInt(args[4]);
                field1ID = getID(row1,col1);
                field2ID = getID(row2,col2);
                for(int i = 0; i < graph.size(); i++){
                    if(graph.get(i).getID() == field1ID){
                        field1 = graph.get(i);

                    } else if(graph.get(i).getID() == field2ID) {
                        field2 = graph.get(i);
                    }
                }
                if(field1.getValue() == field1.getTargetValue()){
                    if(field2.getTargetValue() == field1.getTargetValue()){
                        return true;
                    }else{
                        return false;
                    }
                }
                    return true;
            }catch(NumberFormatException ex){
                System.out.println("wrong data");
                return false;
            }
        }
    }

    boolean stepSizeOneOnly(String move){
        if(this.stepSizeOneOnly == false){
            return true;
        }else{
            String[] args = move.split(";");

            try{
                Field field1 = null;
                int field1ID, field2ID;
                int row1 = Integer.parseInt(args[1]);
                int col1 = Integer.parseInt(args[2]);
                int row2 = Integer.parseInt(args[3]);
                int col2 = Integer.parseInt(args[4]);
                field1ID = getID(row1,col1);
                field2ID = getID(row2,col2);
                for(int i = 0; i < graph.size(); i++){
                    if(graph.get(i).getID() == field1ID){
                        field1 = graph.get(i);
                        break;
                    }
                }
                for(int i = 0; i < field1.Neighbours.size(); i++){
                    if(field1.Neighbours.get(i).getID() == field2ID && field1.Neighbours.get(i).getValue() == Board.BOARD_FIELD){
                        return true;
                    }
                }
                return false;

            }catch(NumberFormatException ex){
                System.out.println("wrong data");
                return false;
            }
        }
    }

    boolean jumpOverOneOnly(String move){
        if(this.jumpOverOneOnly == false){
            return true;
        }else{
            if(stepSizeOneOnly(move) == true)
                return true;
            else{
                String[] args = move.split(";");
                try{
                    Field field1 = null, field2 = null;
                    int field1ID, field2ID;
                    int row1 = Integer.parseInt(args[1]);
                    int col1 = Integer.parseInt(args[2]);
                    int row2 = Integer.parseInt(args[3]);
                    int col2 = Integer.parseInt(args[4]);
                    field1ID = getID(row1,col1);
                    field2ID = getID(row2,col2);
                    for(int i = 0; i < graph.size(); i++){
                        if(graph.get(i).getID() == field1ID){
                            field1 = graph.get(i);
                        } else {
                            if (graph.get(i).getID() == field2ID) {
                                field2 = graph.get(i);
                            }

                        }
                    }


                    return false;
                }catch(NumberFormatException ex){
                    System.out.println("wrong data");
                    return false;
                }
            }

        }
    }

    private int getID(int row, int col){
        int ID = 0;
        if(row > 0) {
            ID = sumFields(row);
        }
        int colCounter;
        for(colCounter = 0; colCounter<=col + 1; colCounter++){
            if(tempBoard[row+1][colCounter] != Board.NOT_PLAYABLE_FIELD){
                ID++;
            }
        }
        return ID;
    }

    private int sumFields(int rowsAmount){
        int sum = 0;
        for(int i = 0; i < rowsAmount; i++){
            sum = sum + fieldsPerRow[i];
        }
        return sum;
    }


    //--------------------------------------------------------------------------------------------
    //BUILDER (FLUENT)
    //--------------------------------------------------------------------------------------------

    public static class Builder{

        private ArrayList<Field> graph;
        private int[] fieldsPerRow;
        private int[][] tempBoard;

        //PickOne
        private boolean stepSizeOneOnly;
        private boolean jumpOverOneOnly;
        private boolean multiJumpsOver;
        //End

        //PickOne
        private boolean canNotEscapeTargetCorner;
        //End

        //PickOne
        private boolean moveOnlyToEmptyField;
        //End

        private boolean moveOnlyYourPieces;
        //...

        public Builder(ArrayList<Field> graph, int[] fieldsPerRow, int[][] tempBoard) {
            this.graph = graph;
            this.fieldsPerRow = fieldsPerRow;
            this.tempBoard = tempBoard;

            this.stepSizeOneOnly = false;
            this.jumpOverOneOnly = false;
            this.multiJumpsOver = false;
            this.canNotEscapeTargetCorner = false;
            this.moveOnlyToEmptyField = false;
            this.moveOnlyYourPieces = false;

        }

        public Builder moveOnlyYourPieces(boolean moveOnlyYourPieces){
            this.moveOnlyYourPieces = moveOnlyYourPieces;
            return this;
        }

        public Builder moveOnlyToEmptyField(boolean moveOnlyToEmptyField){
            this.moveOnlyToEmptyField = moveOnlyToEmptyField;
            return this;
        }

        public Builder stepsizeOneOnly(boolean stepSizeOneOnly){
            this.stepSizeOneOnly = stepSizeOneOnly;
            return this;
        }

        public Builder jumpOverOneOnly(boolean jumpOverOneOnly){
            this.jumpOverOneOnly = jumpOverOneOnly;
            return this;
        }

        public Builder multiJumpsOver(boolean multiJumpsOver){
            this.multiJumpsOver = multiJumpsOver;
            return this;
        }

        public Builder canNotEscapeTargetCorner(boolean canNotEscapeTargetCorner){
            this.canNotEscapeTargetCorner = canNotEscapeTargetCorner;
            return this;
        }
        public Rules build(){
            return new Rules(this);
        }

    }
}

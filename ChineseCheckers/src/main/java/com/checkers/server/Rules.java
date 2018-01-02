package com.checkers.server;

import com.checkers.server.board.Board;
import com.checkers.server.board.Field;
import java.util.ArrayList;

public class Rules {
    ArrayList<Field> graph;
    int[] fieldsPerRow;
    int[][] tempBoard;


    boolean stepSizeOneOnly;
    boolean jumpOverOneOnly;

    boolean multiJumpsOver;
    boolean canNotEscapeTargetCorner;
    //boolean jumpOverGivesPenalty;
    //boolean bigHops;
    //...

    private Rules(final Builder builder){
        this.graph = builder.graph;
        this.fieldsPerRow = builder.fieldsPerRow;
        this.tempBoard = builder.tempBoard;

        this.stepSizeOneOnly = builder.stepSizeOneOnly;
        this.jumpOverOneOnly = builder.jumpOverOneOnly;
        this.multiJumpsOver = builder.multiJumpsOver;
        this.canNotEscapeTargetCorner = builder.canNotEscapeTargetCorner;
        //...
    }

    boolean checkMove(String move){
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

    boolean multiJumpsOver(String move){
        if(this.multiJumpsOver== false){
            return true;
        }else{

            return true; // or false zalezy co wyjdzie z linijek powyzej
        }
    }

    boolean canNotEscapeTargetCorner(String move){
        if(this.canNotEscapeTargetCorner == false){
            return true;
        }else{

            return true; // or false zalezy co wyjdzie z linijek powyzej
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
                    if(field1.Neighbours.get(i).getID() == field2ID){
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
            //sprawdzanie poprawnosci przeskokow
            return true; // or false zalezy co wyjdzie z linijek powyzej
        }
    }



    private int getID(int row, int col){
        int ID = 1;
        if(row > 0) {
            ID = sumFields(row - 1);
        }
        int colCounter;
        for(colCounter = 0; colCounter <= col; colCounter++){
            if(tempBoard[row][colCounter] != Board.NOT_PLAYABLE_FIELD){
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
        //private boolean jumpOverGivesPenalty;
        //End
        //...

        public Builder(ArrayList<Field> graph, int[] fieldsPerRow, int[][] tempBoard) {
            this.graph = graph;
            this.fieldsPerRow = fieldsPerRow;
            this.tempBoard = tempBoard;

            this.stepSizeOneOnly = false;
            this.jumpOverOneOnly = false;
            this.multiJumpsOver = false;
            this.canNotEscapeTargetCorner = false;

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

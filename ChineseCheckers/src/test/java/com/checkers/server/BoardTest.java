package com.checkers.server;


import com.checkers.server.board.Board;
import com.checkers.server.board.WrongData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;


public class BoardTest {


    Board testedBoard = null;

    @BeforeEach
    public void initialize() {

        String line = "newgame;great game;4;3;6;4";
        String args[] = line.split(";");

        try {
            testedBoard = new Board(args);
        } catch (WrongData wrongData) {
            wrongData.printStackTrace();
        }

    }

    @Test
    public void wrongParametersThrowWrongDataException() {

        String line = "newgame;great game;4;3;6;4";
        String args[] = line.split(";");
        args[2]="1";

        try {
            Board board = new Board (args);
            fail("should have failed ");
        } catch (WrongData wrongData) {

        }
    }

    @Test
    public void areCornersAssignedToPlayersProperly(){

        assert testedBoard.getPlayers()==4;
        assert testedBoard.getWhichCorners(0)==2;
        assert testedBoard.getWhichCorners(1)==3;
        assert testedBoard.getWhichCorners(2)==5;
        assert testedBoard.getWhichCorners(3)==6;
    }


    @Test
    public void checkBoardAfterDeletingPlayer(){

        String line = testedBoard.getBoard();
        String board[] = line.split(",");

        assert board[0].equals("0");
        assert board[10].equals(";6");
        assert board[22].equals("2");

        testedBoard.deletePlayer(2);


        String line2 = testedBoard.getBoard();
        String board2[] = line2.split(",");

        assert board2[0].equals(board[0]);
        assert board2[10].equals(board[10]);
        assert !board2[22].equals(board[22]);
        assert board2[22].equals("0");

    }


}
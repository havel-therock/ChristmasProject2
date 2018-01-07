package com.checkers.server;

import com.checkers.server.board.WrongData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {

    Bot testedBot = null;
    Game dummyGame = null;
    Player dummyPlayer = null;

    @BeforeEach
    public void initialize() {

        String line = "newgame;great game;4;3;6;4";
        String args[] = line.split(";");
        dummyPlayer = mock(Player.class);

        try {
            dummyGame = new Game(args, dummyPlayer);
        } catch (WrongData wrongData) {
            wrongData.printStackTrace();
        }

        when(dummyPlayer.getCornerNumber()).thenReturn(1);
        dummyGame.setReady(0);

    }

    @Test
    public void variableTest() {

        testedBot = new Bot(dummyGame);
        testedBot.setBot();

        String name = testedBot.getName();
        assert (name.substring(name.length() - 1).equals(Integer.toString(testedBot.getCornerNumber())));
        assert (testedBot.getName().equals("Bot " + testedBot.getCornerNumber()));

    }

    @Test
    public void threadTest() {

        testedBot = new Bot(dummyGame);
        testedBot.setBot();


        assert (testedBot.listenerThread.isAlive());
        assert testedBot.getBotListener().running == true;
        testedBot.getBotListener().running = false;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert (!testedBot.listenerThread.isAlive());

    }


    @Test
    public void addBotToGame() {

        testedBot = new Bot(dummyGame);
        dummyGame.addPlayer(testedBot);
        testedBot.setBot();

        createPlayers();

        assert dummyGame.getCurrentPlayersNumber() == 4;
        assert dummyGame.getBotsNumber() == 1;
        assert dummyGame.isStarted;

    }

    @Test
    public void removeBot(){


        testedBot = new Bot(dummyGame);
        dummyGame.addPlayer(testedBot);
        testedBot.setBot();

        Player player1;
        player1 = mock(Player.class);
        dummyGame.addPlayer(player1);
        dummyGame.setReady(2);

        dummyGame.deleteBot();

        assert dummyGame.getBotsNumber()==0;

        testedBot = new Bot(dummyGame);
        dummyGame.addPlayer(testedBot);
        testedBot.setBot();

        createPlayers();

        assert dummyGame.isStarted;
        dummyGame.deleteBot();
        assert dummyGame.getBotsNumber()==1;

    }

    @Test
    public void writeToBotTest(){

        testedBot = new Bot(dummyGame);
        dummyGame.addPlayer(testedBot);
        testedBot.setBot();

        createPlayers();

        testedBot.writeToPlayer("Congratulations, you won!");
        assert !testedBot.getIfActive();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert !testedBot.listenerThread.isAlive();
    }



    private void createPlayers() {

        Player player1, player2;
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        dummyGame.addPlayer(player1);
        dummyGame.setReady(2);
        dummyGame.addPlayer(player2);
        dummyGame.setReady(3);

        when(player1.getCornerNumber()).thenReturn(3);
        when(player2.getCornerNumber()).thenReturn(4);

    }



}
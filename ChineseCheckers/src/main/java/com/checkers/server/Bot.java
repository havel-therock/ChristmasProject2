package com.checkers.server;


public class Bot implements User{

    private Game myGame;
    private volatile boolean ifActive;
    private int number;
    private int cornerNumber;
    private String name;
    protected Thread listenerThread;
    private BotListener botListener;


    Bot(Game myGame){
        this.myGame = myGame;
    }

    protected void setBot(){
        this.number = myGame.getNextIndex();
        this.cornerNumber = myGame.getBoard().getWhichCorners(this.number);
        this.name = "Bot "+Integer.toString(cornerNumber);
        this.ifActive = false;

        botListener = new BotListener(this);
        listenerThread = new Thread(botListener);
        listenerThread.start();

        this.myGame.setReady(number);
    }

    protected void move(){
        System.out.println("Hey, I did a very fast move");

       // String move = findMove();
       // myGame.b.executeMove(move);
        //myGame.sendMessage(move);

        setNextPlayerActive();
        setIfActive(false);
        myGame.isWon();
    }

    private String findMove(){
        return "move;2;3;4;12";
    }

    @Override
    public void writeToPlayer(String message) {
        if(message.equals("Congratulations, you won!")){
            System.out.println("Hey, I won");
            this.ifActive = false;
            botListener.running=false;
            myGame.delete(this);
            this.name = "winner";
        }else if(message.equals("gameover")){
            System.out.println("Hey, I lost");
            this.ifActive = false;
            botListener.running=false;
            this.name = "loser";
            myGame.delete(this);
        }
        return;
    }

    @Override
    public void setIfActive(boolean state) {
        this.ifActive = state;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int getCornerNumber() {
        return 0;
    }
    protected boolean getIfActive(){
        return ifActive;
    }

    protected void setNextPlayerActive(){
        if(isNextPlayer()) {
            if (number == myGame.getCurrentPlayersNumber() - 1) {
                myGame.setActivePlayer(0);
            } else {
                myGame.setActivePlayer(number + 1);
            }
        }
        ifActive = false;
    }

    private boolean isNextPlayer(){
        if(myGame.getCurrentPlayersNumber()==1){
            writeToPlayer("Congratulations, you won!");
            return false;
        }else{
            return true;
        }
    }

    public int getNumber(){
        return number;
    }

    protected void closeBot(){
        botListener.running = false;
    }

}

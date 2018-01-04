package com.checkers.server;


public class BotListener implements Runnable {

    Bot bot;
    protected volatile boolean  running;

    BotListener(Bot bot){
        this.bot = bot;
        running = true;
    }

    public void run(){
        while(running) {
            if (bot.getIfActive()) {
                bot.move();
            }
        }
    }


}

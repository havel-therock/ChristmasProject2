package com.test.test1;

import java.io.IOException;
import java.net.ServerSocket;

public class test {

    public static void main(String[] args){
        System.out.println("test1");

        try{
            ServerSocket ser =  new ServerSocket();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


}

package com.checkers.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ServerTest {


    @BeforeEach
    public void initialize() {

    }

    @Test
    public void cannotCreateToInstancesOfServer() {

        CheckersServer server1 ,server2;
        server1 = CheckersServer.getInstance();
        server2 = CheckersServer.getInstance();
        assert server1!=null;
        assert server2==server1;
    }

}
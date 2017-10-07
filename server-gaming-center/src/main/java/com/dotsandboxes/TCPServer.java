/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

import com.dotsandboxes.server.threads.ServerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPServer {

    private Logger LOGGER = LoggerFactory.getLogger(TCPServer.class);

    private ServerThread server;

    public void runServer() {
        server = new ServerThread();
        server.start();
    }

    public void stopServer() {
        if (server != null && server.isAlive()) {
            server.stopServer();
        }
    }

}
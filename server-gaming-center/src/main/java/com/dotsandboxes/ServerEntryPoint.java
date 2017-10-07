/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

public class ServerEntryPoint {

    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.runServer();
    }

}

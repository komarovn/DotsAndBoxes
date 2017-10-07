/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

import java.net.ConnectException;
import java.net.Socket;

public class TCPClient {

    private Socket clientSocket;
    private Boolean isConnected;

    public void runClient() {
        try {
            try {
                clientSocket = new Socket(ClientConstants.SERVER_NAME, ClientConstants.PORT_NUMBER);
                isConnected = true;
                System.out.println("Connection established.");
            } catch (ConnectException e) {
                isConnected = false;
                System.out.println("Connection refused.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public Boolean getConnected() {
        return isConnected;
    }
}
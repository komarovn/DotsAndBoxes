/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.threads.communication;

import com.dotsandboxes.ServerConstants;
import com.dotsandboxes.server.listeners.ResponseListener;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ResponseThread extends Thread implements ResponseListener {

    private Logger LOGGER = LoggerFactory.getLogger(ResponseThread.class);

    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;

    public ResponseThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            while (!clientSocket.isClosed()) {
                try {
                    sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // waiting for response to send it.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendResponse(Response response) {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                objectOutputStream.writeObject(response);
                LOGGER.debug("Client's address: {}: send response: {}", clientSocket.getInetAddress(), response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopResponseThread() {
        Response response = new Response();
        response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
        response.setParameter(ServerConstants.SERVER_STATE, "STOPPED");
        if (!clientSocket.isClosed()) {
            sendResponse(response);
        }
        stop();
    }

}

/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.threads.communication;

import com.dotsandboxes.client.listeners.ResponseListener;
import com.dotsandboxes.shared.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread for receiving response from server. Response can be received in background mode, i.e. without sending request.
 */
public class ResponseThread extends Thread {

    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private List<ResponseListener> listeners = new ArrayList<>();

    public ResponseThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            while (!clientSocket.isClosed()) {
                final Response response = receiveResponse();
                if (response != null) {
                    for (final ResponseListener listener : listeners) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                listener.receiveResponse(response);
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.out.print("Failed to create response thread");
        }
    }

    public void addResponseListener(ResponseListener toAdd) {
        listeners.add(toAdd);
    }

    private Response receiveResponse() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                return (Response) inputStream.readObject();
            }
        } catch (Exception e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}

/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.threads.communication;

import com.dotsandboxes.server.RequestProcessor;
import com.dotsandboxes.server.listeners.ResponseListener;
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RequestThread extends Thread {

    private Logger LOGGER = LoggerFactory.getLogger(RequestThread.class);

    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ResponseListener responseListener;
    private final RequestProcessor requestProcessor;

    public RequestThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.requestProcessor = new RequestProcessor(this);
    }

    @Override
    public void run() {
        try {
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!clientSocket.isClosed()) {
            final Request request = receiveRequest();
            if (request != null) {
                processRequest(request);
            } else {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stop();
            }
        }
    }

    public Request receiveRequest() {
        Request request = null;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                request = (Request) objectInputStream.readObject();
                LOGGER.debug("Client's address: {}, Received request: {}", clientSocket.getInetAddress(), request.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.debug("Request is failed: client's address: {}", clientSocket.getInetAddress());
        }
        return request;
    }

    public void addListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void processRequest(Request request) {
        Response response = new Response();
        requestProcessor.process(request, response);
        sendResponse(response);
    }

    public RequestProcessor getRequestProcessor() {
        return requestProcessor;
    }

    public void sendResponse(Response response) {
        responseListener.sendResponse(response);
    }

    public void stopRequestThread() {
        stop();
    }

}

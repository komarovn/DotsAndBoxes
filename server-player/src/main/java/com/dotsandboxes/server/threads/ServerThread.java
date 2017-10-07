/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.threads;

import com.dotsandboxes.ServerConstants;
import com.dotsandboxes.server.threads.communication.RequestThread;
import com.dotsandboxes.server.threads.communication.ResponseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server thread runs a server, so the main thread will be free for UI frame
 */
public class ServerThread extends Thread {

    private Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);

    private ServerSocket serverSocket = null;
    private static List<Thread> communicationThreads = new ArrayList<>();

    @Override
    public void run() {
        init();
        listenToClients(serverSocket);
    }

    private void init() {
        LOGGER.info("SERVER IS STARTING...");
        try {
            this.serverSocket = new ServerSocket(ServerConstants.PORT_NUMBER);
            LOGGER.info("SERVER HAS STARTED.");
        } catch (IOException ex) {
            LOGGER.info("SERVER START HAS FAILED!");
            ex.printStackTrace();
            System.exit(0);
        }
        LOGGER.info("==== LISTENING FOR PORT {}...", ServerConstants.PORT_NUMBER);
    }

    private void listenToClients(ServerSocket serverSocket) {
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                RequestThread requestThread = new RequestThread(clientSocket);
                ResponseThread responseThread = new ResponseThread(clientSocket);
                requestThread.addListener(responseThread);
                requestThread.start();
                responseThread.start();
                communicationThreads.add(responseThread);
                communicationThreads.add(requestThread);
            } catch (IOException ex) {
                LOGGER.debug("Server socket is closed.");
            }
        }
    }

    public synchronized void stopServer() {
        for (Thread thread : communicationThreads) {
            LOGGER.debug("Communication thread {} is stopped.", thread.getId());
            if (thread instanceof RequestThread) {
                ((RequestThread) thread).stopRequestThread();
            }
            if (thread instanceof ResponseThread) {
                ((ResponseThread) thread).stopResponseThread();
            }
        }
        try {
            serverSocket.close();
            LOGGER.debug("Server thread is stopped.");
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Thread> getCommunicationThreads() {
        return communicationThreads;
    }

}
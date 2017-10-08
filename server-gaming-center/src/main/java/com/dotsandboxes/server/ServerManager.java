/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server;

import com.dotsandboxes.ServerConstants;
import com.dotsandboxes.server.model.GameModel;
import com.dotsandboxes.server.model.UsersModel;
import com.dotsandboxes.server.threads.ServerThread;
import com.dotsandboxes.server.threads.communication.ResponseThread;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Response;

public class ServerManager {
    private UsersModel users;
    private GameModel gameModel;

    public ServerManager() {
        this.users = new UsersModel();
        this.gameModel = new GameModel();
    }

    public UsersModel getUsers() {
        return users;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Send a notification to those users which are waiting for game's creation.
     */
    public void sendNotificationGameCreated() {
        Response response = new Response();
        response.setParameter(ServerConstants.TYPE, MessageType.LOGIN);
        response.setParameter(ServerConstants.IS_GAME_CREATED, true);
        for (Thread thread : ServerThread.getCommunicationResponseThreads()) {
            ((ResponseThread) thread).sendResponse(response);
        }
    }
}

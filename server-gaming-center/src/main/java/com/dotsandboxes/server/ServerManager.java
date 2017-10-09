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
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerManager {

    private Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);
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
        ServerThread.broadcastResponse(response);
        LOGGER.debug("A notification about game was created was sent to all users.");
    }

    /**
     * When a new user connected or one of existing users deisconnected, server
     * sends an updated list of user names
     */
    public void broadcastUserNames() {
        Response response = new Response();
        response.setParameter(ServerConstants.TYPE, MessageType.LOAD_USERS);
        response.setParameter(ServerConstants.LIST_USERS, getUsers().getListOfUsers());
        ServerThread.broadcastResponse(response);
        LOGGER.debug("User names list was sent to all active users.");
    }
}

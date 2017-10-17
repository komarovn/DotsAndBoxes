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
    private GameModel gameModel;

    public ServerManager() {
        this.gameModel = new GameModel(new UsersModel());
    }

    public UsersModel getUsers() {
        return gameModel.getUsers();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Check for any connection to server.
     * @return {@code true} if there is at least one connection to server and {@code false} otherwise.
     */
    public boolean isAnyConnections() {
        return !getUsers().isEmptyUsers();
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
     * When a new user connected or one of existing users disconnected, server
     * sends an updated list of user names
     */
    public void broadcastUserNames() {
        Response response = new Response();
        response.setParameter(ServerConstants.TYPE, MessageType.LOAD_USERS);
        response.setParameter(ServerConstants.LIST_USERS, getUsers().getListOfUsers());
        response.setParameter(ServerConstants.CURRENT_PLAYER, getGameModel().getUsers().getCurrentPlayer());
        ServerThread.broadcastResponse(response);
        LOGGER.debug("User names list was sent to all active users.");
    }

    /**
     * Update gaming board for all active users when some event was fired.
     */
    public void broadcastModelUpdate() {
        Response response = new Response();
        response.setParameter(ServerConstants.TYPE, MessageType.UPDATE_STATE);
        response.setParameter(ServerConstants.CURRENT_PLAYER, getGameModel().getUsers().getCurrentPlayer());
        response.setParameter(ServerConstants.MODEL, getGameModel().convert());
        if (gameModel.isGameOver()) {
            response.setParameter(ServerConstants.WINNER, gameModel.getWinnerName());
        }
        ServerThread.broadcastResponse(response);
        LOGGER.debug("Update model for all active users was processed.");
    }
}

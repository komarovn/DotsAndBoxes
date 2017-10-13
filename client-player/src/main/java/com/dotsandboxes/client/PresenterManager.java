/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client;

import com.dotsandboxes.ClientConstants;
import com.dotsandboxes.client.gui.controller.DotsAndBoxesController;
import com.dotsandboxes.client.gui.controller.LoginController;
import com.dotsandboxes.client.listeners.ResponseListener;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Response;

import java.util.List;

public class PresenterManager<Controller> implements ResponseListener {
    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void receiveResponse(Response response) {
        MessageType type = (MessageType) response.getParameter(ClientConstants.TYPE);
        if (type != null) {
            switch (type) {
                case TRY_CONNECT:
                    processTryConnect(response);
                    break;
                case LOGIN:
                    if (controller instanceof LoginController) {
                        processLoginResponse(response);
                    }
                    break;
                case LOAD_USERS:
                    if (controller instanceof DotsAndBoxesController) {
                        processLoadUsers(response);
                    }
                    break;
                case GAME_SETTINGS:
                    if (controller instanceof DotsAndBoxesController) {
                        processGameSettings(response);
                    }
                    break;
                case UPDATE_STATE:
                    processUpdateBoard(response);
                    break;
                case GAME_OVER:
                    processGameOver(response);
                    break;
                case ADMINISTRATIVE:
                    break;
            }
        }
    }

    private void processTryConnect(Response response) {
        boolean isFirstClient = (boolean) response.getParameter(ClientConstants.IS_FIRST_CLIENT);
        boolean isGameCreated = (boolean) response.getParameter(ClientConstants.IS_GAME_CREATED);
        ((LoginController) controller).setVisibleBoardSizePanel(isFirstClient);
        if (!isFirstClient && !isGameCreated) {
            ((LoginController) controller).applyWaitingState();
        } else {
            ((LoginController) controller).resolveWaitingState();
        }
    }

    private void processLoginResponse(Response response) {
        if (response.getParameter(ClientConstants.IS_GAME_CREATED) != null) {
            boolean isGameCreated = (boolean) response.getParameter(ClientConstants.IS_GAME_CREATED);
            if (isGameCreated) {
                ((LoginController) controller).resolveWaitingState();
            } else {
                ((LoginController) controller).applyWaitingState();
            }
        } else if (response.getParameter(ClientConstants.MESSAGE) != null) {
            String message = (String) response.getParameter(ClientConstants.MESSAGE);
            ((LoginController) controller).openMainFrame((String) response.getParameter(ClientConstants.USER_NAME));
            System.out.printf(message);
        }
    }

    private void processLoadUsers(Response response) {
        List<String> names = (List<String>) response.getParameter(ClientConstants.LIST_USERS);
        String currentPlayer = (String) response.getParameter(ClientConstants.CURRENT_PLAYER);
        ((DotsAndBoxesController) controller).loadUsersData(names);
        ((DotsAndBoxesController) controller).setCurrentPlayer(currentPlayer);
    }

    private void processGameSettings(Response response) {
        int rows = (int) response.getParameter(ClientConstants.BOARD_SIZE_ROWS);
        int cols = (int) response.getParameter(ClientConstants.BOARD_SIZE_COLUMNS);
        String currentPlayer = (String) response.getParameter(ClientConstants.CURRENT_PLAYER);
        ((DotsAndBoxesController) controller).initBoard(rows, cols);
        ((DotsAndBoxesController) controller).setCurrentPlayer(currentPlayer);
    }

    private void processUpdateBoard(Response response) {
    }

    private void processGameOver(Response response) {
        // TODO: response contains an information about who won the game.
    }
}

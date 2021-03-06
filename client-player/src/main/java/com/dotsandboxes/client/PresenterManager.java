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
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PresenterManager<Controller> implements ResponseListener {
    private Controller controller;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

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
                    if (controller instanceof DotsAndBoxesController) {
                        processUpdateBoard(response);
                        if (((DotsAndBoxesController) controller).getOrbRequestListener() != null) {
                            processLoadUsers(response);
                        }
                    }
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
            Request request = new Request(MessageType.UPDATE_STATE);
            request.setParameter(ClientConstants.CLIENT_STATE, ClientConstants.WAITING_FOR_GAME_CREATION);
            retry(request, 250);
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
                Request request = new Request(MessageType.UPDATE_STATE);
                request.setParameter(ClientConstants.CLIENT_STATE, ClientConstants.WAITING_FOR_GAME_CREATION);
                retry(request, 250);
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

        if (((DotsAndBoxesController) controller).getOrbRequestListener() != null) {
            Request request = new Request(MessageType.UPDATE_STATE);
            request.setParameter(ClientConstants.CLIENT_STATE, ClientConstants.UPDATE_MODEL);
            retry(request, 500);
        }
    }

    private void processGameSettings(Response response) {
        int rows = (int) response.getParameter(ClientConstants.BOARD_SIZE_ROWS);
        int cols = (int) response.getParameter(ClientConstants.BOARD_SIZE_COLUMNS);
        String currentPlayer = (String) response.getParameter(ClientConstants.CURRENT_PLAYER);
        List<Object> gameModel = (List<Object>) response.getParameter(ClientConstants.MODEL);

        ((DotsAndBoxesController) controller).initBoard(rows, cols);
        ((DotsAndBoxesController) controller).setCurrentPlayer(currentPlayer);
        ((DotsAndBoxesController) controller).updateBoard(gameModel);
    }

    private void processUpdateBoard(Response response) {
        String currentPlayer = (String) response.getParameter(ClientConstants.CURRENT_PLAYER);
        List<Object> gameModel = (List<Object>) response.getParameter(ClientConstants.MODEL);
        String winner = (String) response.getParameter(ClientConstants.WINNER);

        ((DotsAndBoxesController) controller).setCurrentPlayer(currentPlayer);
        ((DotsAndBoxesController) controller).updateBoard(gameModel);
        if (winner != null) {
            ((DotsAndBoxesController) controller).informGameOver(winner);
        }
    }

    public void retry(final Request request, int delay) {

        Thread asyncTask = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;

                if (controller instanceof LoginController) {
                    response = ((LoginController) controller).getOrbRequestListener().processRequest(request);
                } else if (controller instanceof DotsAndBoxesController) {
                    response = ((DotsAndBoxesController) controller).getOrbRequestListener().processRequest(request);
                }

                final Response finalResponse = response;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        receiveResponse(finalResponse);
                    }
                });
            }
        });

        scheduledExecutorService.schedule(asyncTask, delay, TimeUnit.MILLISECONDS);
    }
}

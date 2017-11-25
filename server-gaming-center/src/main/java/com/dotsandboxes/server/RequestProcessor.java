/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server;

import com.dotsandboxes.Processable;
import com.dotsandboxes.ServerConstants;
import com.dotsandboxes.corbaservice.CorbaServer;
import com.dotsandboxes.server.threads.communication.RequestThread;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One client has his own Request Processor
 */
public class RequestProcessor {
    private Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

    private Processable owner;

    public RequestProcessor(Processable owner) {
        this.owner = owner;
    }

    public void process(Request request, Response response) {
        switch (request.getType()) {
            case TRY_CONNECT:
                processTryConnect(request, response);
                response.setParameter(ServerConstants.TYPE, MessageType.TRY_CONNECT);
                break;
            case LOGIN:
                processLogin(request, response);
                response.setParameter(ServerConstants.TYPE, MessageType.LOGIN);
                break;
            case LOAD_USERS:
                processLoadUsers(request, response);
                response.setParameter(ServerConstants.TYPE, MessageType.LOAD_USERS);
                break;
            case GAME_SETTINGS:
                processGameSettings(request, response);
                response.setParameter(ServerConstants.TYPE, MessageType.GAME_SETTINGS);
                break;
            case CREATE_EDGE:
                processCreateEdge(request, response);
                break;
            case UPDATE_STATE:
                break;
            case GAME_OVER:
                break;
            case ADMINISTRATIVE:
                processAdministrativeRequest(request, response);
                response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
                break;
            default:
                processUnrecognizedMessageType(response);
        }
    }

    private void processTryConnect(Request request, Response response) {
        String userAddress = getUserAddress(request);
        LOGGER.info("Established connection: {}", userAddress);
        response.setParameter(ServerConstants.IS_FIRST_CLIENT, !owner.getServerManager().isAnyConnections());
        response.setParameter(ServerConstants.IS_GAME_CREATED, owner.getServerManager().getGameModel().isGameCreated());
        owner.getServerManager().getUsers().addUser(userAddress, null);
    }

    private void processLogin(Request request, Response response) {
        try {
            String userAddress = getUserAddress(request);
            String userName = (String) request.getParameter(ServerConstants.USER_NAME);
            owner.getServerManager().getUsers().addUser(userAddress, userName);

            if (owner instanceof RequestThread) {
                owner.getServerManager().broadcastUserNames();
            }

            if (!owner.getServerManager().getGameModel().isGameCreated()) {
                int rowsNumber = Integer.parseInt((String) request.getParameter(ServerConstants.BOARD_SIZE_ROWS));
                int colsNumber = Integer.parseInt((String) request.getParameter(ServerConstants.BOARD_SIZE_COLUMNS));
                owner.getServerManager().getGameModel().initGame(rowsNumber, colsNumber);
                LOGGER.info("Game with board size {} by {} was created!", rowsNumber, colsNumber);
                if (owner instanceof RequestThread) {
                    owner.getServerManager().sendNotificationGameCreated();
                }
            }

            response.setParameter(ServerConstants.MESSAGE, "accept\n");
            LOGGER.info("User {} has been connected with address: {}", userName, userAddress);
            response.setParameter(ServerConstants.USER_NAME, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLoadUsers(Request request, Response response) {
        response.setParameter(ServerConstants.LIST_USERS, owner.getServerManager().getUsers().getListOfUsers());
        response.setParameter(ServerConstants.CURRENT_PLAYER, owner.getServerManager().getGameModel().getUsers().getCurrentPlayer());
    }

    private void processGameSettings(Request request, Response response) {
        response.setParameter(ServerConstants.BOARD_SIZE_ROWS, owner.getServerManager().getGameModel().getRows());
        response.setParameter(ServerConstants.BOARD_SIZE_COLUMNS, owner.getServerManager().getGameModel().getColumns());
        response.setParameter(ServerConstants.CURRENT_PLAYER, owner.getServerManager().getGameModel().getUsers().getCurrentPlayer());
        response.setParameter(ServerConstants.MODEL, owner.getServerManager().getGameModel().convert());
    }

    private void processAdministrativeRequest(Request request, Response response) {
        String state = (String) request.getParameter(ServerConstants.CLIENT_STATE);

        if (state != null && state.equals("DISCONNECT")) {
            String userAddress = getUserAddress(request);
            owner.getServerManager().getUsers().removeUser(userAddress);
            LOGGER.info("User with address {} has been disconnected.", userAddress);
            if (owner instanceof RequestThread) {
                owner.getServerManager().broadcastUserNames();
            }

            if (!owner.getServerManager().isAnyConnections()) {
                owner.getServerManager().getGameModel().destroy();
                LOGGER.info("All users are left. Game was destroyed.");
            }
        }
    }

    private String getUserAddress(Request request) {
        String userAddress = "";

        if (owner instanceof RequestThread) {
            userAddress = ((RequestThread) owner).getClientSocket().getInetAddress().getHostAddress() + ":" +
                    ((RequestThread) owner).getClientSocket().getPort();
        } else if (owner instanceof CorbaServer) {
            userAddress = (String) request.getParameter(ServerConstants.CLIENT_ADDRESS);
        }

        return userAddress;
    }

    private void processCreateEdge(Request request, Response response) {
        String sender = getUserAddress(request);
        int leftPoint = (int) request.getParameter(ServerConstants.LEFT_POINT);
        int rightPoint = (int) request.getParameter(ServerConstants.RIGHT_POINT);
        owner.getServerManager().getGameModel().addEdge(leftPoint, rightPoint, sender);
        LOGGER.info("User: {} has created a new edge ({}, {}).", sender, leftPoint, rightPoint);
        if (owner instanceof RequestThread) {
            owner.getServerManager().broadcastModelUpdate();
        }
    }

    private void processUnrecognizedMessageType(Response response) {
        response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
        response.setParameter(ServerConstants.MESSAGE, "Can't recognise your request! Check the guide to provide readable request");
    }

    private void nullMessageType(Response response) {
        response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
        response.setParameter(ServerConstants.MESSAGE, "No message type! Check the guide to provide readable request");
    }
}

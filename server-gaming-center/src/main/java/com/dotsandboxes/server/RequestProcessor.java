/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server;

import com.dotsandboxes.ServerConstants;
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

    private RequestThread owner;

    public RequestProcessor(RequestThread owner) {
        this.owner = owner;
    }

    public void process(Request request, Response response) {
        MessageType type = (MessageType) request.getParameter(ServerConstants.TYPE);
        if (type != null) {
            switch (type) {
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
        } else {
            nullMessageType(response);
        }
    }

    private void processTryConnect(Request request, Response response) {
        String userAddress =  owner.getClientSocket().getInetAddress().getHostAddress() + ":" + owner.getClientSocket().getPort();
        LOGGER.info("Established connection: {}", userAddress);
        response.setParameter(ServerConstants.IS_FIRST_CLIENT, !owner.getServerManager().getUsers().isAnyUsers());
        response.setParameter(ServerConstants.IS_GAME_CREATED, owner.getServerManager().getGameModel().isGameCreated());
        owner.getServerManager().getUsers().addUser(userAddress, null);
    }

    private void processLogin(Request request, Response response) {
        try {
            String userAddress = owner.getClientSocket().getInetAddress().getHostAddress() + ":" + owner.getClientSocket().getPort();
            String userName = (String) request.getParameter(ServerConstants.USER_NAME);
            owner.getServerManager().getUsers().addUser(userAddress, userName);

            owner.getServerManager().broadcastUserNames();

            if (!owner.getServerManager().getGameModel().isGameCreated()) {
                int rowsNumber = Integer.parseInt((String) request.getParameter(ServerConstants.BOARD_SIZE_ROWS));
                int colsNumber = Integer.parseInt((String) request.getParameter(ServerConstants.BOARD_SIZE_COLUMNS));
                owner.getServerManager().getGameModel().setRows(rowsNumber);
                owner.getServerManager().getGameModel().setColumns(colsNumber);
                LOGGER.info("Game with board size {} by {} was created!", rowsNumber, colsNumber);
                owner.getServerManager().sendNotificationGameCreated();
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
    }

    private void processAdministrativeRequest(Request request, Response response) {
        String state = (String) request.getParameter(ServerConstants.CLIENT_STATE);
        if (state != null && state.equals("DISCONNECT")) {
            String userAddress = owner.getClientSocket().getInetAddress().getHostAddress() + ":" + owner.getClientSocket().getPort();
            owner.getServerManager().getUsers().removeUser(userAddress);
            LOGGER.info("User with address {} has been disconnected.", userAddress);
            owner.getServerManager().broadcastUserNames();
        }
    }

    private void processCreateEdge(Request request, Response response) {

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

/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server;

import com.dotsandboxes.ServerConstants;
import com.dotsandboxes.server.threads.ServerThread;
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
    private String userName;

    public RequestProcessor(RequestThread owner) {
        this.owner = owner;
    }

    private void receiveToken(Request request, Response response) {
        try {
            userName = (String) request.getParameter(ServerConstants.USER_NAME);
            response.setParameter(ServerConstants.MESSAGE, "accept");
            LOGGER.info("User {} has been connected", userName);
            response.setParameter(ServerConstants.USER_NAME, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(Request request, Response response) {
        MessageType type = (MessageType) request.getParameter(ServerConstants.TYPE);
        if (type != null) {
            switch (type) {
                case LOGIN:
                    receiveToken(request, response);
                    response.setParameter(ServerConstants.TYPE, MessageType.LOGIN);
                    break;
                case ADMINISTRATIVE:
                    response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
                    break;
                default:
                    unrecognizedMessageType(response);
            }
        } else {
            nullMessageType(response);
        }
    }

    private void unrecognizedMessageType(Response response) {
        response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
        response.setParameter(ServerConstants.MESSAGE, "Can't recognise your request! Check the guide to provide readable request");
    }

    private void nullMessageType(Response response) {
        response.setParameter(ServerConstants.TYPE, MessageType.ADMINISTRATIVE);
        response.setParameter(ServerConstants.MESSAGE, "No message type! Check the guide to provide readable request");
    }
}

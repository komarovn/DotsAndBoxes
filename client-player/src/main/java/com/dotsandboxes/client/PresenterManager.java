/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client;

import com.dotsandboxes.ClientConstants;
import com.dotsandboxes.client.gui.controller.LoginController;
import com.dotsandboxes.client.listeners.ResponseListener;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Response;

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
                case LOGIN:
                    if (controller instanceof LoginController) {
                        processLoginResponse(response);
                    }
                    break;
                case ADMINISTRATIVE:
                    break;
            }
        }
    }

    private void processLoginResponse(Response response) {
        if (response.getParameter(ClientConstants.MESSAGE) != null) {
            String message = (String) response.getParameter(ClientConstants.MESSAGE);
            ((LoginController) controller).openMainFrame((String) response.getParameter(ClientConstants.USER_NAME));
            System.out.printf(message);
        }
    }

}

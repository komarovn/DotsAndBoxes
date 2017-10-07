/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.gui.controller;

import com.dotsandboxes.client.listeners.RequestListener;
import com.dotsandboxes.client.gui.DotsAndBoxes;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DotsAndBoxesController implements Initializable {
    private DotsAndBoxes mainApp;
    private String userName;
    private RequestListener requestListener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMainApp(DotsAndBoxes mainApp) {
        this.mainApp = mainApp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addRequestListener(RequestListener listener) {
        requestListener = listener;
    }
}

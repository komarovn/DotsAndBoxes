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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DotsAndBoxesController implements Initializable {
    private DotsAndBoxes mainApp;
    private String currentUser;
    private RequestListener requestListener;

    private List<Label> users = new ArrayList<Label>();

    @FXML
    private VBox usersPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setMainApp(DotsAndBoxes mainApp) {
        this.mainApp = mainApp;
    }

    public void setUserName(String userName) {
        this.currentUser = userName;
    }

    public void addRequestListener(RequestListener listener) {
        requestListener = listener;
    }

    private Label createUserLabel(String userName) {
        Label userLabel = new Label(userName);

        usersPane.getChildren().add(users.size(), userLabel);
        return userLabel;
    }

    public void loadUsersData(List<String> names) {
        usersPane.getChildren().removeAll(users);
        this.users.clear();
        for (String name : names) {
            this.users.add(createUserLabel(name));
        }
    }
}

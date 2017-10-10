/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.gui.controller;

import com.dotsandboxes.ClientConstants;
import com.dotsandboxes.client.listeners.RequestListener;
import com.dotsandboxes.client.gui.DotsAndBoxes;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Request;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DotsAndBoxesController implements Initializable {
    private final String REGULAR_FONT = "GothamPro-Light";
    private final String MEDIUM_FONT = "GothamPro-Medium";

    private DotsAndBoxes mainApp;
    private String currentUser;
    private RequestListener requestListener;

    private List<Label> users = new ArrayList<Label>();

    @FXML
    private VBox usersPane;

    @FXML
    private GridPane board;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.add(createDot(), 1, 2);
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
        userLabel.setFont(Font.font(REGULAR_FONT));
        userLabel.setPadding(new Insets(0, 0, 8, 16));
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

    public void createNewEdge(int leftPoint, int rightPoint) {
        Request request = new Request(MessageType.CREATE_EDGE);
        request.setParameter(ClientConstants.LEFT_POINT, leftPoint);
        request.setParameter(ClientConstants.RIGHT_POINT, rightPoint);
        requestListener.sendRequest(request);
    }

    private ToggleButton createDot() {
        ToggleButton dot = new ToggleButton("");
        dot.setStyle("-fx-background-radius: 5em; " +
                "-fx-min-width: 16px; " +
                "-fx-min-height: 16px; " +
                "-fx-max-width: 16px; " +
                "-fx-max-height: 16px;" +
                "-fx-background-color: lightpink");
        addDotListener(dot);
        return dot;
    }

    private void addDotListener(final ToggleButton dot) {
        dot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dot.isSelected();
            }
        });
    }
}

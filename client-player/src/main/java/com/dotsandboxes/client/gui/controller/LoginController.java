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
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import com.dotsandboxes.client.gui.DotsAndBoxes;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final String SERVER_UNAVAILABLE = "Server is unavailable now";
    private final String FILL_FIELDS = "Please, fill your name";
    private final String FILL_ALL_FIELDS = "Please, fill your name and size of board";
    private final String PLEASE_WAIT = "Please, wait...";

    private DotsAndBoxes mainApp;
    private boolean isConnected = false;
    private RequestListener requestListener;
    private Response response;

    @FXML
    private Button playButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField userNameTextfield;

    @FXML
    private TextField rowsNumberTextBox;

    @FXML
    private TextField colsNumberTextBox;

    @FXML
    private Label statusLabel;

    @FXML
    private Label rowsLabel;

    @FXML
    private Label columnsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    onPlayAction();
                }
            }
        });
        setVisibleBoardSizePanel(false);
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onPlayAction();
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isConnected) {
                    Request request = new Request(MessageType.ADMINISTRATIVE);
                    request.setParameter(ClientConstants.CLIENT_STATE, "DISCONNECT");
                    requestListener.sendRequest(request);
                }
                System.out.println("App is closed");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void setStatusConnection(boolean isConnected) {
        this.isConnected = isConnected;
        statusLabel.setText(SERVER_UNAVAILABLE);
        statusLabel.setVisible(!isConnected);
        playButton.setDisable(!isConnected);
        userNameTextfield.setDisable(!isConnected);
        rowsNumberTextBox.setDisable(!isConnected);
        colsNumberTextBox.setDisable(!isConnected);
    }

    public void applyWaitingState() {
        setWaitingState(true);
    }

    public void resolveWaitingState() {
        setWaitingState(false);
    }

    private void setWaitingState(boolean isWaiting) {
        statusLabel.setText(PLEASE_WAIT);
        statusLabel.setVisible(isWaiting);
        userNameTextfield.setDisable(isWaiting);
        rowsNumberTextBox.setDisable(isWaiting);
        colsNumberTextBox.setDisable(isWaiting);
        playButton.setDisable(isWaiting);
    }

    public void setVisibleBoardSizePanel(boolean isVisible) {
        colsNumberTextBox.setVisible(isVisible);
        rowsNumberTextBox.setVisible(isVisible);
        columnsLabel.setVisible(isVisible);
        rowsLabel.setVisible(isVisible);
    }

    public void setMainApp(DotsAndBoxes mainApp) {
        this.mainApp = mainApp;
    }

    public void addRequestListener(RequestListener listener) {
        requestListener = listener;
    }

    public void openMainFrame(String userId) {
        statusLabel.setVisible(false);
        mainApp.openMainFrame(userId);
    }

    private void onPlayAction() {
        if (!userNameTextfield.getText().isEmpty()) {
            Request request = new Request(MessageType.LOGIN);
            statusLabel.setVisible(false);

            if (rowsNumberTextBox.isVisible()) {
                if (rowsNumberTextBox.getText().isEmpty() || colsNumberTextBox.getText().isEmpty()) {
                    statusLabel.setText(FILL_ALL_FIELDS);
                    statusLabel.setVisible(true);
                    return;
                } else {
                    request.setParameter(ClientConstants.BOARD_SIZE_ROWS, rowsNumberTextBox.getText());
                    request.setParameter(ClientConstants.BOARD_SIZE_COLUMNS, colsNumberTextBox.getText());
                }
            }

            request.setParameter(ClientConstants.USER_NAME, userNameTextfield.getText());
            requestListener.sendRequest(request);
        }
        else {
            statusLabel.setText(FILL_FIELDS);
            statusLabel.setVisible(true);
        }
    }

}

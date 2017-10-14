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
import com.dotsandboxes.client.util.ColorUtil;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Request;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.*;

public class DotsAndBoxesController implements Initializable {
    private final String REGULAR_FONT = "GothamPro-Light";
    private final String MEDIUM_FONT = "GothamPro-Medium";

    private DotsAndBoxes mainApp;
    private String currentUser;
    private RequestListener requestListener;
    private boolean isMyMove = false;

    private Map<Label, String> users = new HashMap<Label, String>();

    @FXML
    private VBox usersPane;

    @FXML
    private GridPane board;

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
        userLabel.setFont(Font.font(REGULAR_FONT));
        userLabel.setPadding(new Insets(0, 0, 8, 16));
        usersPane.getChildren().add(users.size(), userLabel);
        return userLabel;
    }

    public void loadUsersData(List<String> names) {
        usersPane.getChildren().removeAll(users.keySet());
        this.users.clear();
        for (String name : names) {
            this.users.put(createUserLabel(name), ColorUtil.getNextColor());
        }
    }

    public void setCurrentPlayer(String playerName) {
        isMyMove = currentUser.equals(playerName);
        for (Label user : users.keySet()) {
            user.setFont(user.getText().equals(playerName) ? Font.font(MEDIUM_FONT) : Font.font(REGULAR_FONT));
        }
    }

    public void initBoard(int rows, int cols) {
        for (int i = 0; i <= 2 * cols; i++) {
            for (int j = 0; j <= 2 * rows; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board.add(createDot(), i, j);
                } else if (i % 2 == 1 && j % 2 == 0) {
                    board.add(createHorizontalEdge(), i, j);
                } else if (i % 2 == 0 && j % 2 == 1) {
                    board.add(createVerticalEdge(), i, j);
                } else {
                    board.add(createBox(), i, j);
                }
            }
        }
    }

    public void updateBoard(List<Boolean> dots, List<Boolean> edges, List<String> boxes) {

    }

    public void createNewEdge(int leftPoint, int rightPoint) {
        Request request = new Request(MessageType.CREATE_EDGE);
        request.setParameter(ClientConstants.LEFT_POINT, leftPoint);
        request.setParameter(ClientConstants.RIGHT_POINT, rightPoint);
        requestListener.sendRequest(request);
    }

    private ToggleButton createDot() {
        ToggleButton dot = new ToggleButton("");
        dot.getStyleClass().add("dot-button");
        addDotListener(dot);
        return dot;
    }

    private void addDotListener(final ToggleButton dot) {
        dot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (dot.isSelected()) {
                    Coordinate coordinate = getCoordinatesOfElement(dot);

                }
            }
        });
    }

    private Pane createBox() {
        Pane box = new Pane();
        box.getStyleClass().add("box-panel");
        return box;
    }

    private void colorBox(Pane box, String color) {
        box.setStyle("-fx-background-color: " + color);
    }

    private Pane createVerticalEdge() {
        Pane vedge = new Pane();
        vedge.getStyleClass().add("edge-v-panel");
        return vedge;
    }

    private Pane createHorizontalEdge() {
        Pane hedge = new Pane();
        hedge.getStyleClass().add("edge-h-panel");
        return hedge;
    }

    private Coordinate getCoordinatesOfElement(Node element) {
        Coordinate coordinate = new Coordinate();
        coordinate.y = board.getRowIndex(element);
        coordinate.x = board.getColumnIndex(element);
        return coordinate;
    }

    private void unlockNeighbors(Coordinate currentPosition) {
        List<Node> neighbors = new ArrayList<Node>();
        ObservableList<Node> childs = board.getChildren();

        for (Node child : childs) {
            int x = board.getColumnIndex(child);
            int y = board.getRowIndex(child);
            if ((x == currentPosition.getX() + 2 || x == currentPosition.getX() - 2) &&
                    (y == currentPosition.getY() + 2 || y == currentPosition.getY() - 2)) {
                neighbors.add(child);
            }
        }

        for (Node node : neighbors) {
            if (node instanceof ToggleButton) {
                if (!node.isDisabled()) {

                }
            }
        }
    }

    private void lockAllDots() {
        ObservableList<Node> childs = board.getChildren();
        for (Node child : childs) {
            if (child instanceof ToggleButton) {
                child.setDisable(true);
            }
        }
    }

    private class Coordinate {
        int x;
        int y;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}

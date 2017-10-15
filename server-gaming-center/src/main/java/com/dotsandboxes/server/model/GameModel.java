/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private UsersModel users;

    private Integer rows;
    private Integer columns;

    /**
     * boxes.get(i) - the user's address which is owner of box i.
     * If null, box i doesn't have its owner.
     */
    private List<String> boxes = new ArrayList<String>();
    /**
     * dots.get(i) - is dot i active?
     */
    private List<Boolean> dots = new ArrayList<Boolean>();
    /**
     * edges.get(i) - is edge i active?
     */
    private List<Boolean> edges = new ArrayList<Boolean>();

    public GameModel(UsersModel users) {
        this.users = users;
    }

    public UsersModel getUsers() {
        return users;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public boolean isGameCreated() {
        return rows != null && columns != null;
    }

    public void initGame(Integer rows, Integer columns) {
        this.rows = rows;
        this.columns = columns;

        for (int i = 0; i <= 2 * columns; i++) {
            for (int j = 0; j <= 2 * rows; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    dots.add(true);
                } else if ((i % 2 == 1 && j % 2 == 0) ||
                        (i % 2 == 0 && j % 2 == 1)) {
                    edges.add(true);
                } else {
                    boxes.add(null);
                }
            }
        }
    }

    public void addEdge(int leftPoint, int rightPoint, String userName) {
        String userAddress = getUsers().getUserAddressByName(userName);

    }

    @Deprecated
    public List<Boolean> getDots() {
        return dots;
    }

    @Deprecated
    public List<Boolean> getEdges() {
        return edges;
    }

    @Deprecated
    public List<String> getBoxes() {
        List<String> result = new ArrayList<String>();
        for (String address : boxes) {
            result.add(address == null ? null : getUsers().getUserNameByAddress(address));
        }
        return result;
    }

    /**
     * Convert game's model to a single list for clients.
     * @return a list of necessary objects for updating a gaming board on clients.
     */
    public List<Object> convert() {
        List<Object> model = new ArrayList<Object>();
        int boxesCounter = 0;
        int dotsCounter = 0;
        int edgesCounter = 0;

        for (int i = 0; i <= 2 * columns; i++) {
            for (int j = 0; j <= 2 * rows; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    model.add(dots.get(dotsCounter));
                    dotsCounter++;
                } else if (i % 2 == 1 && j % 2 == 1) {
                    model.add(boxes.get(boxesCounter));
                    boxesCounter++;
                } else {
                    model.add(edges.get(edgesCounter));
                    edgesCounter++;
                }
            }
        }

        return model;
    }

    public void destroy() {
        this.rows = null;
        this.columns = null;
        this.boxes.clear();
        this.edges.clear();
        this.dots.clear();
    }
}

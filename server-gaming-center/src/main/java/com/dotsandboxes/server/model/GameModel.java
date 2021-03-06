/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameModel {
    Logger logger = LoggerFactory.getLogger(GameModel.class);

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
        int jLeft = leftPoint % (columns + 1);
        int iLeft = leftPoint / (columns + 1);
        int jRight = rightPoint % (columns + 1);
        int iRight = rightPoint / (columns + 1);

        Set<Integer> neighborsLeft = new HashSet<Integer>();
        neighborsLeft.add(getTopNeighbor(iLeft, jLeft));
        neighborsLeft.add(getBottomNeighbor(iLeft, jLeft));
        neighborsLeft.add(getLeftNeighbor(iLeft, jLeft));
        neighborsLeft.add(getRightNeighbor(iLeft, jLeft));

        Set<Integer> neighborsRight = new HashSet<Integer>();
        neighborsRight.add(getTopNeighbor(iRight, jRight));
        neighborsRight.add(getBottomNeighbor(iRight, jRight));
        neighborsRight.add(getLeftNeighbor(iRight, jRight));
        neighborsRight.add(getRightNeighbor(iRight, jRight));

        Set<Integer> commonNeighbors = new HashSet<Integer>(neighborsLeft);
        commonNeighbors.retainAll(neighborsRight);

        int commonEdge = -1;
        for (Integer neighbor : commonNeighbors) {
            if (!neighbor.equals(-1)) {
                commonEdge = neighbor;
            }
        }

        logger.trace("New edge: {}.", commonEdge);
        edges.set(commonEdge, false);

        updateDots();
        if (!updateBoxes()) {
            users.updateCurrentPlayer();
        }
    }

    private int getTopNeighbor(int i, int j) {
        return j == 0 ? -1 : j * rows + (j - 1) * (rows + 1) + i;
    }

    private int getBottomNeighbor(int i, int j) {
        return j == columns ? -1 : (j + 1) * rows + j * (rows + 1) + i;
    }

    private int getLeftNeighbor(int i, int j) {
        return i == 0 ? -1 : j * rows + j * (rows + 1) + i - 1;
    }

    private int getRightNeighbor(int i, int j) {
        return i == rows ? -1 : j * rows + j * (rows + 1) + i;
    }

    private void updateDots() {
        for (int dot = 0; dot < dots.size(); dot++) {
            int j = dot / (rows + 1);
            int i = dot % (rows + 1);

            List<Integer> neighbors = new ArrayList<Integer>();
            neighbors.add(getTopNeighbor(i, j));
            neighbors.add(getBottomNeighbor(i, j));
            neighbors.add(getLeftNeighbor(i, j));
            neighbors.add(getRightNeighbor(i, j));

            int countOfInactiveEdges = 0;
            for (Integer neighbor : neighbors) {
                if (neighbor.equals(-1) || !edges.get(neighbor)) {
                    countOfInactiveEdges++;
                }
            }

            if (countOfInactiveEdges == neighbors.size()) {
                dots.set(dot, false);
            }
        }
    }

    /**
     * Update a set of boxes in game model.
     *
     * @return {@code true} if during of adding a new edge one box has got
     * an owner, {@code false} - otherwise.
     */
    private boolean updateBoxes() {
        boolean isNamedBoxCreated = false;
        for (int box = 0; box < boxes.size(); box++) {
            int j = box % rows;
            int i = box / rows;

            List<Integer> neighborsEdges = new ArrayList<Integer>();
            neighborsEdges.add((i + 1) * rows + i * (rows + 1) + j); // top
            neighborsEdges.add((i + 1) * rows + i * (rows + 1) + j + 1); // bottom
            neighborsEdges.add(i * rows + i * (rows + 1) + j); // left
            neighborsEdges.add((i + 1) * rows + (i + 1) * (rows + 1) + j); // right

            if (boxes.get(i * rows + j) == null) {
                int countOfInactiveEdges = 0;

                for (Integer neighborEdge : neighborsEdges) {
                    if (!edges.get(neighborEdge)) {
                        countOfInactiveEdges++;
                    }
                }

                if (countOfInactiveEdges == 4) {
                    isNamedBoxCreated = true;
                    boxes.set(i * rows + j, users.getUserAddressByName(users.getCurrentPlayer()));
                    logger.trace("Box ({}, {}) has became named.", i, j);
                }
            }
        }
        return isNamedBoxCreated;
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
                    String boxOwner = boxes.get(boxesCounter);
                    model.add(boxOwner == null ? null : users.getUserNameByAddress(boxOwner));
                    boxesCounter++;
                } else {
                    model.add(edges.get(edgesCounter));
                    edgesCounter++;
                }
            }
        }

        return model;
    }

    public boolean isGameOver() {
        boolean isGameOver = true;

        for (String owner : boxes) {
            if (owner == null) {
                isGameOver = false;
            }
        }

        return isGameOver;
    }

    public String getWinnerName() {
        Map<String, Integer> scores = new HashMap<String, Integer>();

        for (String playerName : users.getListOfUsers()) {
            scores.put(playerName, 0);
        }

        for (String playerId : boxes) {
            String playerName = users.getUserNameByAddress(playerId);
            scores.put(users.getUserNameByAddress(playerId), scores.get(playerName) + 1);
        }

        Map.Entry<String, Integer> bestEntry = null;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (bestEntry == null || bestEntry.getValue() < entry.getValue()) {
                bestEntry = entry;
            }
        }

        return bestEntry.getKey();
    }

    public void destroy() {
        this.rows = null;
        this.columns = null;
        this.boxes.clear();
        this.edges.clear();
        this.dots.clear();
    }
}

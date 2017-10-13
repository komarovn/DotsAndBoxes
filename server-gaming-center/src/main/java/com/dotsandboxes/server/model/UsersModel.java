/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.model;

import java.util.*;

public class UsersModel {

    /**
     * Address and name of user
     */
    private Map<String, String> users = new LinkedHashMap<String, String>();
    private String currentPlayer;

    public void addUser(String address, String name) {
        if (isEmptyUsers()) {
            currentPlayer = address;
        }
        users.put(address, name);
    }

    public void removeUser(String userAddress) {
        users.remove(userAddress);
    }

    @Deprecated
    public void removeUserByName(String name) {
        for (Map.Entry<String, String> entry : users.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(name)) {
                users.remove(entry.getKey());
                break;
            }
        }
    }

    /**
     * Check for any users in storage.
     * @return {@code true} if storage is empty and {@code false} otherwise.
     */
    public boolean isEmptyUsers() {
        return users.isEmpty();
    }

    /**
     * Check for any active players which can see a gaming board.
     * @return {@code true} if there is at least one player which can see a gaming board
     * and can play the game at the moment, and {@code false} otherwise.
     */
    public boolean isAnyPlayers() {
        for (String name : users.values()) {
            if (name != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get list of all active users.
     * @return names of active players.
     */
    public List<String> getListOfUsers() {
        List<String> names = new ArrayList<String>();

        for (String name : users.values()) {
            if (name != null) {
                names.add(name);
            }
        }

        return names;
    }

    public String getCurrentPlayer() {
        return users.get(currentPlayer);
    }

    /**
     * Give a move to the next player after currentPlayer.
     */
    public void updateCurrentPlayer() {
        //currentPlayer
    }
}

/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersModel {

    /**
     * Address and name of user
     */
    private Map<String, String> users = new HashMap<String, String>();

    public void addUser(String address, String name) {
        users.put(address, name);
    }

    public void removeUser(String userAddress) {
        users.remove(userAddress);
    }

    public void removeUserByName(String name) {
        for (Map.Entry<String, String> entry : users.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(name)) {
                users.remove(entry.getKey());
                break;
            }
        }
    }

    public boolean isAnyUsers() {
        return !users.isEmpty();
    }

    public List<String> getListOfUsers() {
        List<String> names = new ArrayList<String>();

        for (String name : users.values()) {
            if (name != null) {
                names.add(name);
            }
        }

        return names;
    }

}

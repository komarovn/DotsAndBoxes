/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.server.model;

public class GameModel {
    private UsersModel users;

    private Integer rows;
    private Integer columns;

    public GameModel(UsersModel users) {
        this.users = users;
    }

    public UsersModel getUsers() {
        return users;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public boolean isGameCreated() {
        return rows != null && columns != null;
    }

    public void addEdge(int leftPoint, int rightPoint, String user) {

    }
}

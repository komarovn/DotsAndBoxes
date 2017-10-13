/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

public abstract class ServerConstants {
    public static final String INCORRECT_LAUNCH = "Please, specify a size of a board in format 'n m', " +
            "where 'n' is a number of edges in row and 'm' is a number of edges in column";

    public static final int PORT_NUMBER = 59342;

    public static final String TYPE = "type";

    public static final String BOARD_SIZE_ROWS = "rowsNumber";
    public static final String BOARD_SIZE_COLUMNS = "colsNumber";
    public static final String IS_GAME_CREATED = "isGameCreated";
    public static final String IS_FIRST_CLIENT = "isFirstClient";
    public static final String USER_NAME = "userName";
    public static final String LIST_USERS = "listOfUsers";
    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String LEFT_POINT = "leftPoint";
    public static final String RIGHT_POINT = "rightPoint";
    public static final String CLIENT_STATE = "clientState";
    public static final String SERVER_STATE = "serverState";
    public static final String MESSAGE = "message";
}

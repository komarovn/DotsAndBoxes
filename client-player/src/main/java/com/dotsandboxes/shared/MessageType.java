/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.shared;

public enum MessageType {
    ADMINISTRATIVE,
    LOGIN,
    TRY_CONNECT,
    LOAD_USERS,
    CREATE_EDGE,
    UPDATE_STATE,
    GAME_OVER
}

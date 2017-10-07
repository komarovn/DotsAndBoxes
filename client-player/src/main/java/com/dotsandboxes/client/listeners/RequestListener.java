/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.listeners;

import com.dotsandboxes.shared.Request;

public interface RequestListener {

    void sendRequest(Request request);

}

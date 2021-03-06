/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.listeners;

import com.dotsandboxes.shared.Response;

public interface ResponseListener {

    void receiveResponse(Response response);

}

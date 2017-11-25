/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.ServicePOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CorbaServiceImpl extends ServicePOA {
    private Logger LOGGER = LoggerFactory.getLogger(CorbaServiceImpl.class);

    @Override
    public String startCorbaService() {
        return "\nCorba Service was started!\n";
    }

    @Override
    public String processRequest(String request) {
        LOGGER.trace("Received synchronous request: {}", request);
        //send req

        return request;
    }

    @Override
    public void sendRequest(String request) {
        LOGGER.trace("Received one-way request: {}", request);
        // TODO: implement one-way communication.
    }
}
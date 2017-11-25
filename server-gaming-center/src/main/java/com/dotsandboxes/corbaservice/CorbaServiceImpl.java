/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.ServicePOA;
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CorbaServiceImpl extends ServicePOA {
    private CorbaServer owner;

    private Logger LOGGER = LoggerFactory.getLogger(CorbaServiceImpl.class);

    public void setOwner(CorbaServer owner) {
        this.owner = owner;
    }

    @Deprecated
    public String startCorbaService() {
        return "\nCorba Service was started!\n";
    }

    @Override
    public byte[] processRequest(byte[] request) {
        Request requestObj = (Request) SerializationUtils.deserialize(request);
        LOGGER.trace("Received synchronous request: {}", requestObj);
        Response response = new Response();

        owner.getRequestProcessor().process(requestObj, response);

        return SerializationUtils.serialize(response);
    }

    @Override
    public void sendRequest(byte[] request) {
        Request requestObj = (Request) SerializationUtils.deserialize(request);
        LOGGER.trace("Received one-way request: {}", requestObj);

        owner.getRequestProcessor().process(requestObj, new Response());
    }
}
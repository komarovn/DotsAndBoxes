/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.*;
import com.dotsandboxes.shared.Request;
import com.dotsandboxes.shared.Response;
import org.apache.commons.lang.SerializationUtils;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class CorbaClient {
    private static CorbaClient instance = new CorbaClient();
    private static Service serviceImpl;

    private CorbaClient() { }

    public static CorbaClient getInstance() {
        return instance;
    }

    public boolean init(String ... args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "CorbaService";
            serviceImpl = ServiceHelper.narrow(ncRef.resolve_str(name));

            System.out.println("ORB service is ready.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Synchronous communication.
     *
     * @param request - request to be sent.
     * @return response object.
     */
    public Response processRequest(Request request) {
        byte[] requestSerialized = SerializationUtils.serialize(request);
        byte[] responseSerialized = serviceImpl.processRequest(requestSerialized);
        return (Response) SerializationUtils.deserialize(responseSerialized);
    }

    /**
     * One-way communication.
     *
     * @param request - request to be sent.
     */
    public void sendRequest(Request request) {
        serviceImpl.sendRequest(SerializationUtils.serialize(request));
    }
}
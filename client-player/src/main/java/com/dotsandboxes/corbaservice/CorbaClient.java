/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class CorbaClient {
    private static CorbaClient instance = new CorbaClient();
    private static Service serviceImpl;

    private CorbaClient() { }

    public static CorbaClient getInstance() {
        return instance;
    }

    public void init(String ... args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "CorbaService";
            serviceImpl = ServiceHelper.narrow(ncRef.resolve_str(name));

            System.out.println(serviceImpl.startCorbaService());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
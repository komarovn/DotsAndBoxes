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
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorbaServer {
    private static CorbaServer instance = new CorbaServer();
    private ORB orb;

    private Logger LOGGER = LoggerFactory.getLogger(CorbaServer.class);

    private CorbaServer() { }

    public static CorbaServer getInstance() {
        return instance;
    }

    public void init(String ... args) {
        try {
            this.orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            CorbaServiceImpl serviceImpl = new CorbaServiceImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(serviceImpl);

            Service href = ServiceHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "CorbaService";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            LOGGER.info("Server was initialized.");
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if (orb == null) {
            LOGGER.warn("Server was not initialized.");
        } else {
            orb.run();
            LOGGER.info("Server was started.");
        }
    }

    public void shutdown() {
        if (orb != null) {
            LOGGER.info("Server Exiting ...");
            orb.shutdown(false);
            LOGGER.info("Server was stopped.");
        }
    }

}

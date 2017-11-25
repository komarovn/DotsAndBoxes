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

public class CorbaServer {
    private static CorbaServer instance = new CorbaServer();

    private CorbaServer() { }

    public static CorbaServer getInstance() {
        return instance;
    }

    public void init(String ... args) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            CorbaServiceImpl serviceImpl = new CorbaServiceImpl();
            serviceImpl.setORB(orb);
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(serviceImpl);

            Service href = ServiceHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "CorbaService";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);
            System.out.println("Server ready and waiting ...");
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Server Exiting ...");
    }

}

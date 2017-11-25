package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class CorbaClient {
    static Service serviceImpl;

    public static void main(String args[]) {
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
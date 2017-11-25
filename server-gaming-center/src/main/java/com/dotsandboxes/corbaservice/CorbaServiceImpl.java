package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.ServicePOA;
import org.omg.CORBA.*;

class CorbaServiceImpl extends ServicePOA {
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public String startCorbaService() {
        return "\nCorba Service was started!\n";
    }

    public String processRequest(String request) {
        //send req
        return "ioioi";
    }

    public void shutdownService() {
        orb.shutdown(false);
    }
}
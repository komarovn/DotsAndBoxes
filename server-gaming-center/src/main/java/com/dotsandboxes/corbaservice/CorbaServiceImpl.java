/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.corbaservice;

import com.dotsandboxes.corbaservice.service.ServicePOA;
import org.omg.CORBA.*;

class CorbaServiceImpl extends ServicePOA {
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public String startCorbaService() {
        return "\nCorba Service was started!\n";
    }

    @Override
    public String processRequest(String request) {
        //send req
        return "ioioi";
    }

    @Override
    public void shutdownService() {
        orb.shutdown(false);
    }
}
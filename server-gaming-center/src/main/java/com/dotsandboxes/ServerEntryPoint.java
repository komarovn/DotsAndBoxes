/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

import com.dotsandboxes.corbaservice.CorbaServer;

public class ServerEntryPoint {

    public static void main(String[] args) {
        boolean isTcp = true;

        if (args.length > 1 && "-s".equals(args[0])) {
            isTcp = !"corba".equals(args[1]);
        }

        if (isTcp) {
            TCPServer server = new TCPServer();
            server.runServer();
        } else {
            CorbaServer server = CorbaServer.getInstance();
            server.init("-ORBInitialPort", String.valueOf(ServerConstants.ORB_PORT));
        }
    }

}

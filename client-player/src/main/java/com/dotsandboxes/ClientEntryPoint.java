/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes;

import com.dotsandboxes.client.gui.DotsAndBoxes;
import javafx.application.Application;

public class ClientEntryPoint {

    public static void main(String ... args) {
        boolean isTcp = true;

        if (args.length > 1 && "-s".equals(args[0])) {
            isTcp = !"corba".equals(args[1]);
        }

        Application.launch(DotsAndBoxes.class, String.valueOf(isTcp));
    }

}

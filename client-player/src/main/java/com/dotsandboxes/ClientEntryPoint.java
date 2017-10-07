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
        if (args.length == 0) {
            System.out.println(StringResourses.INCORRECT_LAUNCH);
            return;
        }
        String host = args[0];
        Application.launch(DotsAndBoxes.class);
    }

}

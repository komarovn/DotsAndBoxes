/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.util;

import java.util.Arrays;
import java.util.List;

public class ColorUtil {
    private static int index = 0;

    /**
     * A list of predefined colors for boxes. One user has its own color.
     */
    public static final List<String> boxColors = Arrays.asList(
            "#e8505b",
            "#eeb35d",
            "#779ed9",
            "#41bb7d",
            "#fdd835",
            "#c6d1e4",
            "#c51162",
            "#78909c");

    public static String getNextColor() {
        String result;
        if (boxColors.size() > index) {
            result = boxColors.get(index);
        } else {
            result = boxColors.get(boxColors.size() - 1);
        }
        index++;
        return result;
    }

}

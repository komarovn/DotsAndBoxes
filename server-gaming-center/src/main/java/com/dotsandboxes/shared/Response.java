/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.shared;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private HashMap<String, Object> data;

    public Response() {
        this.data = new HashMap<>();
    }

    public void setParameter(String key, Object value) {
        data.put(key, value);
    }

    public Object getParameter(String key) {
        return data.get(key);
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                "}";
    }
}
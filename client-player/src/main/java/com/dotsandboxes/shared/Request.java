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

public class Request implements Serializable {

    private HashMap<String, Object> data;
    private MessageType type;

    public Request(MessageType type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public void setParameter(String key, Object value) {
        data.put(key, value);
    }

    public Object getParameter(String key) {
        return data.get(key);
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Request{" +
                "data=" + data +
                '}';
    }
}
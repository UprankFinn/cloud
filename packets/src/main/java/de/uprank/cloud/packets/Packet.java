package de.uprank.cloud.packets;

import java.io.Serializable;

public class Packet implements Serializable {

    private final String key;
    private final Object object;

    public Packet(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public Object getObject() {
        return object;
    }
}

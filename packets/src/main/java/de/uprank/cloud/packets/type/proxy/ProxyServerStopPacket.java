package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;

public class ProxyServerStopPacket implements Serializable {

    private final String name;

    public ProxyServerStopPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

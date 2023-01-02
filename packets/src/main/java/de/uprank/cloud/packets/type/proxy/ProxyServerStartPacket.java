package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;

public class ProxyServerStartPacket implements Serializable {

    private final String name;

    public ProxyServerStartPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

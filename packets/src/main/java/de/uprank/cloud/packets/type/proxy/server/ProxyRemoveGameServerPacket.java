package de.uprank.cloud.packets.type.proxy.server;

import java.io.Serializable;

public class ProxyRemoveGameServerPacket implements Serializable {

    private final String name;

    public ProxyRemoveGameServerPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

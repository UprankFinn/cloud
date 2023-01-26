package de.uprank.cloud.packets.type.proxy.register;

import java.io.Serializable;

public class ProxyRegisteredPacket implements Serializable {

    private final String name;

    public ProxyRegisteredPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

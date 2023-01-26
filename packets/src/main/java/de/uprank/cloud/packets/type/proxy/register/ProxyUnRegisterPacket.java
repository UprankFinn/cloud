package de.uprank.cloud.packets.type.proxy.register;

import java.io.Serializable;

public class ProxyUnRegisterPacket implements Serializable {

    private final String name;

    public ProxyUnRegisterPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

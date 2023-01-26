package de.uprank.cloud.packets.type.proxy.server;

import java.io.Serializable;

public class ProxyAddGameServerPacket implements Serializable {

    private final String name;

    private final String hostName;
    private final Integer port;

    public ProxyAddGameServerPacket(String name, String hostName, Integer port) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }
}

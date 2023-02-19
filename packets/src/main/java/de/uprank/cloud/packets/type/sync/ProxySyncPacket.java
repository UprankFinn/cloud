package de.uprank.cloud.packets.type.sync;

import de.uprank.cloud.packets.util.TrioMap;

import java.io.Serializable;

public class ProxySyncPacket implements Serializable {

    private final String name;

    private final String serverName;
    private final String address;
    private final Integer port;
    public ProxySyncPacket(String name, String serverName, String address, Integer port) {
        this.name = name;
        this.serverName = serverName;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getServerName() {
        return serverName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }
}

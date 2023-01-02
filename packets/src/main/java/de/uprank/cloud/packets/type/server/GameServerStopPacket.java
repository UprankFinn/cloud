package de.uprank.cloud.packets.type.server;

import java.io.Serializable;

public class GameServerStopPacket implements Serializable {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final String wrapper;

    public GameServerStopPacket(String name, String hostName, Integer port, String wrapper) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.wrapper = wrapper;
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

    public String getWrapper() {
        return wrapper;
    }
}

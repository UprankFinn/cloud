package de.uprank.cloud.packets.type.wrapper;

import java.io.Serializable;

public class WrapperStopPacket implements Serializable {

    private final String name;
    private final String hostName;

    public WrapperStopPacket(String name, String hostName) {
        this.name = name;
        this.hostName = hostName;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }
}

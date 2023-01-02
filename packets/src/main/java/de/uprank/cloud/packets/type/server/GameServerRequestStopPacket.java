package de.uprank.cloud.packets.type.server;

import java.io.Serializable;

public class GameServerRequestStopPacket implements Serializable {

    private final String name;
    private final String wrapper;

    public GameServerRequestStopPacket(String name, String wrapper) {
        this.name = name;
        this.wrapper = wrapper;
    }

    public String getName() {
        return name;
    }

    public String getWrapper() {
        return wrapper;
    }
}

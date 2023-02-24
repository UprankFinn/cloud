package de.uprank.cloud.packets.type.server;

import java.io.Serializable;

public class GameServerUnsyncPacket implements Serializable {

    private final String name;

    public GameServerUnsyncPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

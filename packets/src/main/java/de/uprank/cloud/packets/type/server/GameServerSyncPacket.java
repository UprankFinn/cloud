package de.uprank.cloud.packets.type.server;

import java.io.Serializable;

public class GameServerSyncPacket implements Serializable {

    private final String name;

    public GameServerSyncPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

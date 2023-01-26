package de.uprank.cloud.packets.type.player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerLoginPacket implements Serializable {

    private final UUID uniqueId;
    private final String name;

    public PlayerLoginPacket(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }
}

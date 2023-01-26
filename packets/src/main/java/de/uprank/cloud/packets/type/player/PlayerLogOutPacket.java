package de.uprank.cloud.packets.type.player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerLogOutPacket implements Serializable {

    private final UUID uniqueId;
    private final String name;

    public PlayerLogOutPacket(UUID uniqueId, String name) {
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

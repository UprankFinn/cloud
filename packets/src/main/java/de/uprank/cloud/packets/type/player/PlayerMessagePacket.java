package de.uprank.cloud.packets.type.player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerMessagePacket implements Serializable {

    private final UUID uniqueId;
    private final String message;

    public PlayerMessagePacket(UUID uniqueId, String message) {
        this.uniqueId = uniqueId;
        this.message = message;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getMessage() {
        return message;
    }
}

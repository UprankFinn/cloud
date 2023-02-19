package de.uprank.cloud.packets.type.player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerSwitchServerPacket implements Serializable {

    private final UUID uniqueId;

    private final String proxy;
    private final String server;

    public PlayerSwitchServerPacket(UUID uniqueId, String proxy, String server) {
        this.uniqueId = uniqueId;
        this.proxy = proxy;
        this.server = server;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getProxy() {
        return proxy;
    }

    public String getServer() {
        return server;
    }
}

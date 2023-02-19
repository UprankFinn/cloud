package de.uprank.cloud.packets.type.server;

import java.io.Serializable;
import java.util.Map;

public class GameServerUpdatePacket implements Serializable {

    private final String serverName;

    private final Map<String, Object> updatedData;

    public GameServerUpdatePacket(String serverName, Map<String, Object> updatedData) {
        this.serverName = serverName;
        this.updatedData = updatedData;
    }

    public String getServerName() {
        return serverName;
    }

    public Map<String, Object> getUpdatedData() {
        return updatedData;
    }
}

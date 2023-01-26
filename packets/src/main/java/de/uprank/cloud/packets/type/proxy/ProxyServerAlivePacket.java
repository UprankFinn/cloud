package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ProxyServerAlivePacket implements Serializable {

    private final String name;

    private final Integer onlinePlayers;
    private final List<UUID> onlineUniqueIds;

    public ProxyServerAlivePacket(String name, Integer onlinePlayers, List<UUID> onlineUniqueIds) {
        this.name = name;

        this.onlinePlayers = onlinePlayers;
        this.onlineUniqueIds = onlineUniqueIds;
    }

    public String getName() {
        return name;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public List<UUID> getOnlineUniqueIds() {
        return onlineUniqueIds;
    }
}

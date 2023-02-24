package de.uprank.cloud.packets.type.server;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class GameServerUpdatePlayerPacket implements Serializable {

    private final List<UUID> online;
    private final Integer maxPlayerCount;


    public GameServerUpdatePlayerPacket(List<UUID> online, Integer maxPlayerCount) {
        this.online = online;
        this.maxPlayerCount = maxPlayerCount;
    }

    public List<UUID> getOnline() {
        return online;
    }

    public Integer getMaxPlayerCount() {
        return maxPlayerCount;
    }
}

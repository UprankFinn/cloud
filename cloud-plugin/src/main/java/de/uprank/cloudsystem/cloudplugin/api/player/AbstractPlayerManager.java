package de.uprank.cloudsystem.cloudplugin.api.player;

import de.uprank.cloudsystem.cloudapi.player.Player;
import de.uprank.cloudsystem.cloudapi.player.PlayerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AbstractPlayerManager implements PlayerManager {

    private final Map<UUID, Player> cloudPlayers;

    public AbstractPlayerManager() {
        this.cloudPlayers = new HashMap<>();
    }

    @Override
    public List<Player> getCloudPlayer() {
        return (List<Player>) this.cloudPlayers;
    }

    @Override
    public Player getCloudPlayer(UUID uniqueId) {
        if (this.cloudPlayers.containsKey(uniqueId)) return this.cloudPlayers.get(uniqueId);
        return null;
    }

    @Override
    public Player getCloudPlayer(String name) {
        return null;
    }
}

package de.uprank.cloudsystem.cloudplugin.api.gameserver;

import com.google.common.collect.Maps;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import de.uprank.cloudsystem.cloudapi.server.GameServerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractGameServerManager implements GameServerManager {

    private final Map<String, AbstractGameServer> gameServers;

    public AbstractGameServerManager() {
        this.gameServers = new HashMap<>();
    }

    @Override
    public List<GameServer> getGameServer() {
        return new ArrayList<>(this.gameServers.values());
    }

    @Override
    public GameServer getGameServer(String name) {
        if (this.gameServers.containsKey(name)) return this.gameServers.get(name);
        return null;
    }

    public Map<String, AbstractGameServer> getGameServers() {
        return gameServers;
    }

    @Override
    public void requestGameServer(String group, String template) {

    }
}

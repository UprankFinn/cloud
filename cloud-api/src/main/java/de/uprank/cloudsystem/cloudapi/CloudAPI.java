package de.uprank.cloudsystem.cloudapi;

import de.uprank.cloudsystem.cloudapi.player.PlayerManager;
import de.uprank.cloudsystem.cloudapi.proxy.ProxyServerManager;
import de.uprank.cloudsystem.cloudapi.server.GameServerManager;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class CloudAPI {

    private static CloudAPI instance;

    public CloudAPI() {
        instance = this;
    }

    public abstract Map<UUID, String> getOnlinePlayers();

    public abstract String getName();
    public abstract String getGameId();
    public abstract String getCurrentTemplate();
    public abstract String getCurrentServerGroup();

    public static CloudAPI getInstance() {
        return instance;
    }

    public abstract void startNewService();

    public abstract Jedis getJedis();

    public abstract PlayerManager getPlayerManager();
    public abstract ProxyServerManager getProxyServerManager();
    public abstract GameServerManager getGameServerManager();

}

package de.uprank.cloudsystem.cloudapi;

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

    public abstract String getCurrentServiceName();
    public abstract String getGameId();
    public abstract String getCurrentTemplate();
    public abstract String getCurrentServerGroup();

    public static CloudAPI getInstance() {
        return instance;
    }

    public abstract void startNewService();

    public abstract List<String> getOnlineProxies();
    public abstract List<String> getOnlineServers();

    public abstract Jedis getJedis();

}

package de.uprank.cloudsystem.cloudapi;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

public abstract class CloudAPI {

    private static CloudAPI instance;

    public CloudAPI() {
        instance = this;
    }

    public abstract Integer getOnlinePlayerCount();
    public abstract List<UUID> getOnlineUniqueIds();
    public abstract String getCurrentServiceName();
    public abstract String getGameId();
    public abstract String getCurrentTemplate();
    public abstract String getCurrentServerGroup();

    public static CloudAPI getInstance() {
        return instance;
    }

    public abstract void startNewService();

    public abstract Jedis getJedis();

}

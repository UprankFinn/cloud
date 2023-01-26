package de.uprank.cloudsystem.cloudapi.events;

import net.md_5.bungee.api.plugin.Event;

public class GameServerStartEvent extends Event {

    private final String server;
    private final String hostName;
    private final Integer port;

    public GameServerStartEvent(String server, String hostName, Integer port) {
        this.server = server;
        this.hostName = hostName;
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }
}

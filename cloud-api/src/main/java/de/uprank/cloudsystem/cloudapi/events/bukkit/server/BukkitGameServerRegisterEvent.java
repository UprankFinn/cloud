package de.uprank.cloudsystem.cloudapi.events.bukkit.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitGameServerRegisterEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    private final String name;
    private final String replayId;

    private final String hostName;
    private final Integer port;

    private final String wrapper;
    private final String group;
    private final String template;

    public BukkitGameServerRegisterEvent(String name, String replayId, String hostName, Integer port, String wrapper, String group, String template) {
        this.name = name;
        this.replayId = replayId;

        this.hostName = hostName;
        this.port = port;

        this.wrapper = wrapper;
        this.group = group;
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public String getReplayId() {
        return replayId;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getGroup() {
        return group;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

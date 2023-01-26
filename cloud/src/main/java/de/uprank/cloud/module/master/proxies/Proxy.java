package de.uprank.cloud.module.master.proxies;

import io.netty.channel.Channel;

import java.util.List;
import java.util.UUID;

public class Proxy {

    private final String name;

    private Integer onlinePlayers;
    private List<UUID> onlineUniqueIds;

    private Channel channel;

    /*public Proxy(String name, Integer onlinePlayers, List<UUID> onlineUniqueIds, Channel channel) {
        this.name = name;
        this.onlinePlayers = onlinePlayers;
        this.onlineUniqueIds = onlineUniqueIds;
        this.channel = channel;
    }*/

    public Proxy(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public Channel getChannel() {
        return channel;
    }
}

package de.uprank.cloud.module.master.proxies;

import io.netty.channel.Channel;

public class Proxy {

    private final String name;

    private Channel channel;

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

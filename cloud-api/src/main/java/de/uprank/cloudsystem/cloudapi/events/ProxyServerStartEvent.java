package de.uprank.cloudsystem.cloudapi.events;

import net.md_5.bungee.api.plugin.Event;

public class ProxyServerStartEvent extends Event {

    private final String proxy;
    private final String hostName;
    private final Integer port;

    public ProxyServerStartEvent(String proxy, String hostName, Integer port) {
        this.proxy = proxy;
        this.hostName = hostName;
        this.port = port;
    }

    public String getProxy() {
        return proxy;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }
}

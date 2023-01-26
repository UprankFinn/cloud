package de.uprank.cloudsystem.cloudapi.events;

import net.md_5.bungee.api.plugin.Event;

public class ProxyServerStopEvent extends Event {

    private final String proxy;

    public ProxyServerStopEvent(String proxy) {
        this.proxy = proxy;
    }

    public String getProxy() {
        return proxy;
    }
}

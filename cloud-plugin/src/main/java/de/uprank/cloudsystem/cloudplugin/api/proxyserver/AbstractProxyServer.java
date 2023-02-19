package de.uprank.cloudsystem.cloudplugin.api.proxyserver;

import de.uprank.cloudsystem.cloudapi.proxy.ProxyServer;

public class AbstractProxyServer extends ProxyServer {

    private final String name;

    private Integer playerAmount;

    public AbstractProxyServer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getPlayerAmount() {
        return this.playerAmount;
    }
}

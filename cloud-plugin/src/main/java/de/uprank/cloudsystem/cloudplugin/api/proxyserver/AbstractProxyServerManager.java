package de.uprank.cloudsystem.cloudplugin.api.proxyserver;

import de.uprank.cloudsystem.cloudapi.proxy.ProxyServer;
import de.uprank.cloudsystem.cloudapi.proxy.ProxyServerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractProxyServerManager implements ProxyServerManager {

    private final Map<String, AbstractProxyServer> proxyServers;

    public AbstractProxyServerManager() {
        this.proxyServers = new HashMap<>();
    }

    @Override
    public List<ProxyServer> getProxyServer() {
        return this.proxyServers.values().stream().collect(Collectors.toList());
    }

    @Override
    public ProxyServer getProxyServer(String name) {
        if (this.proxyServers.containsKey(name)) return this.proxyServers.get(name);
        return null;
    }

    @Override
    public void requestProxyServer(String group, String template) {

    }
}

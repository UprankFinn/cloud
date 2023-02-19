package de.uprank.cloudsystem.cloudapi.proxy;

import de.uprank.cloudsystem.cloudapi.server.GameServer;

import java.util.List;
import java.util.Map;

public interface ProxyServerManager {

    List<ProxyServer> getProxyServer();
    ProxyServer getProxyServer(String name);

    void requestProxyServer(String group, String template);

}

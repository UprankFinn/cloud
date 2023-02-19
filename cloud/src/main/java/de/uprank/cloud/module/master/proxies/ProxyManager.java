package de.uprank.cloud.module.master.proxies;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.servers.Server;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyManager {

    private final MasterModule masterModule;

    private final Map<String, Proxy> proxyChannels = new HashMap<>();
    private final Map<String, Proxy> proxies = new HashMap<>();

    public ProxyManager(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    public Proxy getProxy(String name) {
        for (Proxy proxy : this.proxies.values()) {
            if (proxy.getName().equals(name)) {
                return proxy;
            }
        }
        return null;
    }

}

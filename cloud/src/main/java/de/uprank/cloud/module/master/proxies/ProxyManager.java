package de.uprank.cloud.module.master.proxies;

import de.uprank.cloud.module.master.MasterModule;
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
}

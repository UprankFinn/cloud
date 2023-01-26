package de.uprank.cloud.module.wrapper.group.proxy;

import java.util.HashMap;
import java.util.Map;

public class ProxyGroupManager {

    private final Map<String, ProxyGroup> proxyGroups = new HashMap<>();


    public Map<String, ProxyGroup> getProxyGroups() {
        return proxyGroups;
    }
}

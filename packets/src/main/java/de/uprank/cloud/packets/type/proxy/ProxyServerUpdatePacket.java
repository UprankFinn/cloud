package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;
import java.util.Map;

public class ProxyServerUpdatePacket implements Serializable {

    private final String proxyName;

    private final Map<String, Object> updatedData;

    public ProxyServerUpdatePacket(String proxyName, Map<String, Object> updatedData) {
        this.proxyName = proxyName;
        this.updatedData = updatedData;
    }

    public String getProxyName() {
        return proxyName;
    }

    public Map<String, Object> getUpdatedData() {
        return updatedData;
    }
}

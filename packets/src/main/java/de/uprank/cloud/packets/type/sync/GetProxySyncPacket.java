package de.uprank.cloud.packets.type.sync;

import java.io.Serializable;

public class GetProxySyncPacket implements Serializable {

    private final String proxyName;

    public GetProxySyncPacket(String proxyName) {
        this.proxyName = proxyName;
    }

    public String getProxyName() {
        return proxyName;
    }
}

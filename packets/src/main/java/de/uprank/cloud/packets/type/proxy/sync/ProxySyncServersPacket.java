package de.uprank.cloud.packets.type.proxy.sync;

import de.uprank.cloud.packets.util.TrioMap;

import java.io.Serializable;

public class ProxySyncServersPacket implements Serializable {

    private final String name;

    private TrioMap<String, String, Integer> servers;

    public ProxySyncServersPacket(String name, TrioMap<String, String, Integer> servers) {
        this.name = name;
        this.servers = servers;
    }

    public String getName() {
        return name;
    }

    public TrioMap<String, String, Integer> getServers() {
        return servers;
    }

    public void setServers(TrioMap<String, String, Integer> servers) {
        this.servers = servers;
    }
}

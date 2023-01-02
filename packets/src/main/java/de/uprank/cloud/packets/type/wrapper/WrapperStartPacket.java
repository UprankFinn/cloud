package de.uprank.cloud.packets.type.wrapper;

import java.io.Serializable;
import java.util.List;

public class WrapperStartPacket implements Serializable {

    private final String name;

    private final String hostName;
    private final Integer maxMemory;

    private final List<String> servergroups;
    private final List<String> proxyGroups;
    private final List<String> lobbyGroups;

    public WrapperStartPacket(String name, String hostName, Integer maxMemory, List<String> servergroups, List<String> proxyGroups, List<String> lobbyGroups) {
        this.name = name;

        this.hostName = hostName;
        this.maxMemory = maxMemory;

        this.servergroups = servergroups;
        this.proxyGroups = proxyGroups;
        this.lobbyGroups = lobbyGroups;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public List<String> getServergroups() {
        return servergroups;
    }

    public List<String> getProxyGroups() {
        return proxyGroups;
    }

    public List<String> getLobbyGroups() {
        return lobbyGroups;
    }
}

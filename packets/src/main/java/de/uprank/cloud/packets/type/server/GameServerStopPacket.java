package de.uprank.cloud.packets.type.server;

import de.uprank.cloud.packets.util.StopReason;

import java.io.Serializable;

public class GameServerStopPacket implements Serializable {

    private final String name;
    private final StopReason stopReason;

    private final String group;
    private final String template;

    private final String wrapper;

    private final String hostName;
    private final Integer port;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final Boolean isProxy;
    private final Boolean isFallBack;
    private final Boolean isDynamic;

    public GameServerStopPacket(String name, StopReason stopReason, String group, String template, String wrapper, String hostName, Integer port, Integer minMemory, Integer maxMemory, Boolean isProxy, Boolean isFallBack, Boolean isDynamic) {
        this.name = name;
        this.stopReason = stopReason;

        this.group = group;
        this.template = template;

        this.wrapper = wrapper;

        this.hostName = hostName;
        this.port = port;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.isProxy = isProxy;
        this.isFallBack = isFallBack;
        this.isDynamic = isDynamic;
    }

    public String getName() {
        return name;
    }

    public StopReason getStopReason() {
        return stopReason;
    }

    public String getGroup() {
        return group;
    }

    public String getTemplate() {
        return template;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getMinMemory() {
        return minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public Boolean getProxy() {
        return isProxy;
    }

    public Boolean getFallBack() {
        return isFallBack;
    }

    public Boolean getDynamic() {
        return isDynamic;
    }
}

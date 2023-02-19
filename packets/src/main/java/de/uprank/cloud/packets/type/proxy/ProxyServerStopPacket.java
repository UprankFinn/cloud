package de.uprank.cloud.packets.type.proxy;

import de.uprank.cloud.packets.util.StopReason;

import java.io.Serializable;

public class ProxyServerStopPacket implements Serializable {

    private final String name;
    private final StopReason stopReason;

    private final String hostName;
    private final Integer port;

    private final String group;
    private final String template;

    private final String wrapper;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final Boolean isDynamic;

    public ProxyServerStopPacket(String name, StopReason stopReason, String hostName, Integer port, String group, String template, String wrapper, Integer minMemory, Integer maxMemory, Boolean isDynamic) {
        this.name = name;
        this.stopReason = stopReason;

        this.hostName = hostName;
        this.port = port;

        this.group = group;
        this.template = template;

        this.wrapper = wrapper;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.isDynamic = isDynamic;
    }

    public String getName() {
        return name;
    }

    public StopReason getStopReason() {
        return stopReason;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getGroup() {
        return group;
    }

    public String getTemplate() {
        return template;
    }

    public Integer getMinMemory() {
        return minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public Boolean getDynamic() {
        return isDynamic;
    }
}

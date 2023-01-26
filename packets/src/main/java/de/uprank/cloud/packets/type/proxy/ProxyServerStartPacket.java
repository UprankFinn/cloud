package de.uprank.cloud.packets.type.proxy;

import de.uprank.cloud.packets.ServerUtil;

import java.io.Serializable;

public class ProxyServerStartPacket implements Serializable {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final String wrapper;
    private final String group;
    private final String template;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final ServerUtil serverUtil;

    private final Boolean isProxy;
    private final Boolean isDynamic;

    public ProxyServerStartPacket(String name, String hostName, Integer port, String wrapper, String group, String template, Integer minMemory, Integer maxMemory, ServerUtil serverUtil, Boolean isProxy, Boolean isDynamic) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.wrapper = wrapper;
        this.group = group;
        this.template = template;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.serverUtil = serverUtil;

        this.isProxy = isProxy;
        this.isDynamic = isDynamic;
    }

    public String getName() {
        return name;
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

    public ServerUtil getServerUtil() {
        return serverUtil;
    }

    public Boolean getProxy() {
        return isProxy;
    }

    public Boolean getDynamic() {
        return isDynamic;
    }
}

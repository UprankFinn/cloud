package de.uprank.cloud.packets.type.server;

import de.uprank.cloud.packets.ServerUtil;

import java.io.Serializable;

public class GameServerStartPacket implements Serializable {

    private final String name;
    private final String replayId;

    private final String hostName;
    private final Integer port;

    private final String wrapper;
    private final String group;
    private final String template;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final ServerUtil serverUtil;

    private final Boolean isProxy;
    private final Boolean isFallBack;
    private final Boolean isDynamic;

    public GameServerStartPacket(String name, String replayId, String hostName, Integer port, String wrapper, String group, String template, Integer minMemory, Integer maxMemory, ServerUtil serverUtil, Boolean isProxy, Boolean isFallBack, Boolean isDynamic) {
        this.name = name;
        this.replayId = replayId;

        this.hostName = hostName;
        this.port = port;

        this.wrapper = wrapper;
        this.group = group;
        this.template = template;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.serverUtil = serverUtil;

        this.isProxy = isProxy;
        this.isFallBack = isFallBack;
        this.isDynamic = isDynamic;
    }

    public String getName() {
        return name;
    }

    public String getReplayId() {
        return replayId;
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

    public Boolean isProxy() {
        return isProxy;
    }

    public Boolean isFallBack() {
        return isFallBack;
    }

    public Boolean isDynamic() {
        return isDynamic;
    }
}

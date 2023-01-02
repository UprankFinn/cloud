package de.uprank.cloud.packets.type.server;

import java.io.Serializable;

public class GameServerRequestPacket implements Serializable {

    private final String group;
    private final String template;
    private final String wrapper;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final Boolean isProxy;
    private final Boolean isFallBack;
    private final Boolean isDynamic;

    public GameServerRequestPacket(String group, String template, String wrapper, Integer minMemory, Integer maxMemory, Boolean isProxy, Boolean isFallBack, Boolean isDynamic) {
        this.group = group;
        this.template = template;
        this.wrapper = wrapper;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.isProxy = isProxy;
        this.isFallBack = isFallBack;
        this.isDynamic = isDynamic;
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

    public Integer getMinMemory() {
        return minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public Boolean isProxy(){
        return isProxy;
    }

    public Boolean isFallBack() {
        return isFallBack;
    }

    public Boolean isDynamic() {
        return isDynamic;
    }
}

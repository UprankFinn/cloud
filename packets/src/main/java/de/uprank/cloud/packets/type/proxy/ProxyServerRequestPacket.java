package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;

public class ProxyServerRequestPacket implements Serializable {

    private final String group;
    private final String template;
    private final String wrapper;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final Boolean isProxy;
    private final Boolean isDynamic;

    public ProxyServerRequestPacket(String group, String template, String wrapper, Integer minMemory, Integer maxMemory, Boolean isProxy, Boolean isDynamic) {
        this.group = group;
        this.template = template;
        this.wrapper = wrapper;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.isProxy = isProxy;
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

    public Boolean isDynamic() {
        return isDynamic;
    }

}

package de.uprank.cloud.module.master.proxies;

import io.netty.channel.Channel;

import java.util.List;
import java.util.UUID;

public class Proxy {

    private final String name;

    private String hostName;
    private Integer port;

    private String group;
    private String template;
    private String wrapper;

    private Integer minMemory;
    private Integer maxMemory;

    private Boolean isDynamic;

    private Integer onlinePlayers;
    private List<UUID> onlineUniqueIds;

    private Channel channel;

    /*public Proxy(String name, Integer onlinePlayers, List<UUID> onlineUniqueIds, Channel channel) {
        this.name = name;
        this.onlinePlayers = onlinePlayers;
        this.onlineUniqueIds = onlineUniqueIds;
        this.channel = channel;
    }*/

    public Proxy(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getWrapper() {
        return wrapper;
    }

    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getMinMemory() {
        return minMemory;
    }

    public void setMinMemory(Integer minMemory) {
        this.minMemory = minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Integer maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Boolean getDynamic() {
        return isDynamic;
    }

    public void setDynamic(Boolean dynamic) {
        isDynamic = dynamic;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public List<UUID> getOnlineUniqueIds() {
        return onlineUniqueIds;
    }

    public void setOnlineUniqueIds(List<UUID> onlineUniqueIds) {
        this.onlineUniqueIds = onlineUniqueIds;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

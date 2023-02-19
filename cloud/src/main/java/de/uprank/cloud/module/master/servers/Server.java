package de.uprank.cloud.module.master.servers;

import de.uprank.cloud.module.master.servers.sign.Sign;
import de.uprank.cloud.packets.Packet;
import io.netty.channel.Channel;

public class Server {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final Boolean isFallBack;

    private String wrapper;

    private String group;
    private String template;

    private Integer players;
    private Integer maxPlayers;
    private String motd;
    private String serverState;

    private Sign sign;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final Boolean isDynamic;

    private final Channel channel;

    public Server(String name, String hostName, Integer port, Boolean isFallBack, Integer minMemory, Integer maxMemory, Boolean isDynamic, Channel channel) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.isFallBack = isFallBack;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.isDynamic = isDynamic;

        this.channel = channel;
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

    public Boolean isFallBack() {
        return isFallBack;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return motd;
    }

    public String getServerState() {
        return serverState;
    }

    public void setServerState(String serverState) {
        this.serverState = serverState;
    }

    public String getWrapper() {
        return wrapper;
    }

    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
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

    public Channel getChannel() {
        return channel;
    }

    public Boolean getFallBack() {
        return isFallBack;
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

    public void sendPacket(Packet packet) {
        new Thread(() -> {
            if (this.channel != null) {
                this.channel.writeAndFlush(packet);
            }
        }).start();
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Sign getSign() {
        return sign;
    }
}

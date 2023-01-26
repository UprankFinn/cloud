package de.uprank.cloud.module.master.servers;

import de.uprank.cloud.module.master.sign.Sign;
import de.uprank.cloud.packets.Packet;
import io.netty.channel.Channel;

public class Server {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final Boolean isFallBack;
    private final Sign sign;

    private final Channel channel;

    public Server(String name, String hostName, Integer port, Boolean isFallBack, Sign sign, Channel channel) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.isFallBack = isFallBack;
        this.sign = sign;

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

    public Sign getSign() {
        return sign;
    }

    public Channel getChannel() {
        return channel;
    }

    public void sendPacket(Packet packet) {
        new Thread(() -> {
            if (this.channel != null) {
                this.channel.writeAndFlush(packet);
            }
        }).start();
    }

}

package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyRemoveGameServerPacket;
import de.uprank.cloudsystem.cloudapi.CloudAPI;
import de.uprank.cloudsystem.cloudplugin.CloudCore;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.commands.CloudServerCommand;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.NettyServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.Channel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CloudBukkitPlugin extends JavaPlugin {

    @Getter
    private static CloudBukkitPlugin instance;

    private final CloudCore cloudCore;

    private Channel channel;
    private NettyServer nettyServer;

    public CloudBukkitPlugin() {
        instance = this;
        this.cloudCore = new CloudCore("service.json");
    }

    @Override
    public void onEnable() {

        this.nettyServer = new NettyServer(this);
        new Thread(this.nettyServer).start();

        new CloudServerCommand(this);

    }

    @Override
    public void onDisable() {

        ProxyRemoveGameServerPacket proxyRemoveGameServerPacket = new ProxyRemoveGameServerPacket(this.cloudCore.getCurrentServiceName());
        this.channel.writeAndFlush(new Packet(PacketType.ProxyRemoveGameServerPacket.name(), proxyRemoveGameServerPacket));

        if (this.channel.isOpen() && this.channel.isActive() && this.channel.isRegistered()) {
            this.channel.close();
        }

    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

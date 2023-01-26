package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloudsystem.cloudplugin.CloudCore;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.commands.CloudServerCommand;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.NettyServer;
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

        this.startNetty();

        new CloudServerCommand(this);

    }

    @Override
    public void onDisable() {

        ProxyRemoveGameServerPacket proxyRemoveGameServerPacket = new ProxyRemoveGameServerPacket(this.cloudCore.getCurrentServiceName());
        this.channel.writeAndFlush(new Packet(PacketType.ProxyRemoveGameServerPacket.name(), proxyRemoveGameServerPacket));

        GameServerRequestPacket gameServerRequestPacket = new GameServerRequestPacket(this.getCloudCore().getServergroup(),
                this.getCloudCore().getTemplate(),
                this.getCloudCore().getWrapper(),
                this.getCloudCore().getMinMemory(),
                this.getCloudCore().getMaxMemory(),
                this.getCloudCore().getIsProxy(),
                this.getCloudCore().getIsFallBack(),
                this.getCloudCore().getIsDynamic());
        this.channel.writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), gameServerRequestPacket));

        GameServerStopPacket gameServerStopPacket = new GameServerStopPacket(this.cloudCore.getCurrentServiceName(), this.cloudCore.getHostName(), this.cloudCore.getPort(), this.cloudCore.getWrapper());
        this.channel.writeAndFlush(new Packet(PacketType.GameServerStopPacket.name(), gameServerStopPacket));

        if (this.channel.isOpen() && this.channel.isActive() && this.channel.isRegistered()) {
            this.channel.close();
        }

    }

    public void startNetty(){
        this.nettyServer = new NettyServer(this);
        new Thread(this.nettyServer).start();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

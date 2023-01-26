package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStartPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudBukkitPlugin plugin;

    public NettyHandler(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

        ProxyAddGameServerPacket proxyAddGameServerPacket = new ProxyAddGameServerPacket(this.plugin.getCloudCore().getCurrentServiceName(), Bukkit.getServer().getIp(), Bukkit.getServer().getPort());
        channelHandlerContext.writeAndFlush(new Packet(PacketType.ProxyAddGameServerPacket.name(), proxyAddGameServerPacket));

        this.plugin.setChannel(channelHandlerContext.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {

            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.PlayerLoginPacket.name())) {
                PlayerLoginPacket loginPacket = (PlayerLoginPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().put(loginPacket.getUniqueId(), loginPacket.getName());
            } else if (packet.getKey().equals(PacketType.PlayerLogOutPacket.name())) {
                PlayerLogOutPacket playerLogOutPacket = (PlayerLogOutPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().remove(playerLogOutPacket.getUniqueId());

            } else if (packet.getKey().equals(PacketType.GameServerStartPacket.name())) {
                GameServerStartPacket gameServerStartPacket = (GameServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getOnlineServers().add(gameServerStartPacket.getName());

            } else if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();

                this.plugin.getCloudCore().getOnlineServers().remove(gameServerStopPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {
                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getOnlineProxies().add(proxyServerStartPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {
                ProxyServerStopPacket proxyServerStopPacket = (ProxyServerStopPacket) packet.getObject();

                this.plugin.getCloudCore().getOnlineProxies().remove(proxyServerStopPacket.getName());

            }

        });
    }
}

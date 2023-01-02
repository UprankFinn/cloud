package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyAddGameServerPacket;
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

    }
}

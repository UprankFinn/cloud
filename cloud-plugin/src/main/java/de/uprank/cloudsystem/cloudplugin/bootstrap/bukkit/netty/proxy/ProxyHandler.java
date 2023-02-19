package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.proxy;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.ProxyServerRequestPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStartPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloudsystem.cloudplugin.api.proxyserver.AbstractProxyServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProxyHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudBukkitPlugin plugin;

    public ProxyHandler(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setProxyChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            System.out.println(packet.getKey());

            if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {
                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getProxyServerManager().getProxyServer().add(new AbstractProxyServer(proxyServerStartPacket.getName()));

            } else if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {
                ProxyServerStopPacket proxyServerStopPacket = (ProxyServerStopPacket) packet.getObject();

                this.plugin.getCloudCore().getProxyServerManager().getProxyServer().remove(proxyServerStopPacket.getName());

            }

        }).start();

    }
}

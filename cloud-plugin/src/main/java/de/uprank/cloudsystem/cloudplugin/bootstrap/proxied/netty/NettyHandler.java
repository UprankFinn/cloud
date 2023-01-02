package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStartPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public NettyHandler(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerStartPacket.name(), new ProxyServerStartPacket(this.plugin.getCloudCore().getCurrentServiceName())));

        this.plugin.setChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerStopPacket.name(), new ProxyServerStopPacket(this.plugin.getCloudCore().getCurrentServiceName())));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {

            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.ProxyAddGameServerPacket.name())) {

                ProxyAddGameServerPacket proxyAddGameServerPacket = (ProxyAddGameServerPacket) packet.getObject();

                ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(proxyAddGameServerPacket.getName(), new InetSocketAddress(proxyAddGameServerPacket.getHostName(), proxyAddGameServerPacket.getPort()), null, false);
                ProxyServer.getInstance().getServers().put(proxyAddGameServerPacket.getName(), serverInfo);

                if (this.plugin.getProxyConfig().isNotify().equals(true)) {

                    ProxyServer.getInstance().getPlayers().forEach((players) -> {

                        if (players.hasPermission(this.plugin.getProxyConfig().getNotifyPermission())) {

                            players.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + this.plugin.getProxyConfig().getNotifyServiceStart().replace("%service%", proxyAddGameServerPacket.getName())));

                        }

                    });

                }

                System.out.println("asdas xdddddddddddddddddddddddddddddddddddd");

            } else if (packet.getKey().equals(PacketType.ProxyRemoveGameServerPacket.name())) {

                ProxyRemoveGameServerPacket gameServerPacket = (ProxyRemoveGameServerPacket) packet.getObject();

                ProxyServer.getInstance().getServers().remove(gameServerPacket.getName());

                if (this.plugin.getProxyConfig().isNotify().equals(true)) {

                    ProxyServer.getInstance().getPlayers().forEach((players) -> {

                        if (players.hasPermission(this.plugin.getProxyConfig().getNotifyPermission())) {

                            players.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + this.plugin.getProxyConfig().getNotifyServiceStop().replace("%service%", gameServerPacket.getName())));

                        }

                    });

                }

            }

        }).start();
    }
}

package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloud.packets.type.proxy.*;
import de.uprank.cloud.packets.type.proxy.register.ProxyRegisteredPacket;
import de.uprank.cloud.packets.type.proxy.register.ProxyUnRegisterPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.proxy.sync.ProxySyncServersPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloudsystem.cloudapi.events.GameServerStartEvent;
import de.uprank.cloudsystem.cloudapi.events.GameServerStopEvent;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public NettyHandler(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerStartPacket.name(), new ProxyServerStartPacket(this.plugin.getCloudCore().getCurrentServiceName(),
                this.plugin.getCloudCore().getHostName(), this.plugin.getCloudCore().getPort(), this.plugin.getCloudCore().getWrapper(),
                this.plugin.getCloudCore().getServergroup(), this.plugin.getCloudCore().getTemplate(), this.plugin.getCloudCore().getMinMemory(),
                this.plugin.getCloudCore().getMaxMemory(), ServerUtil.READY, true, this.plugin.getCloudCore().getIsDynamic())));
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyRegisteredPacket.name(), new ProxyRegisteredPacket(this.plugin.getCloudCore().getCurrentServiceName())));

        this.plugin.setChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerStopPacket.name(), new ProxyServerStopPacket(this.plugin.getCloudCore().getCurrentServiceName())));
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyUnRegisteredPacket.name(), new ProxyUnRegisterPacket(this.plugin.getCloudCore().getCurrentServiceName())));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {

            Packet packet = (Packet) object;

            System.out.println(packet.getKey());

            if (packet.getKey().equals(PacketType.ProxySyncServersPacket.name())) {
                ProxySyncServersPacket proxySyncServersPacket = (ProxySyncServersPacket) packet.getObject();

                System.out.println(proxySyncServersPacket.getName() + ":" + proxySyncServersPacket.getServers().getSecond() + ":" + proxySyncServersPacket.getServers().getThird());

                ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(proxySyncServersPacket.getServers().getFirst(), new InetSocketAddress(proxySyncServersPacket.getServers().getSecond(), proxySyncServersPacket.getServers().getThird()), null, false);
                ProxyServer.getInstance().getServers().put(proxySyncServersPacket.getName(), serverInfo);

            } else if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {
                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();

                this.plugin.getProxies().add(proxyServerStartPacket.getName());

                System.out.println("[" + packet.getKey() + "] " + proxyServerStartPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyAddGameServerPacket.name())) {

                ProxyAddGameServerPacket proxyAddGameServerPacket = (ProxyAddGameServerPacket) packet.getObject();

                this.plugin.getProxy().getPluginManager().callEvent(new GameServerStartEvent(proxyAddGameServerPacket.getName(), proxyAddGameServerPacket.getHostName(), proxyAddGameServerPacket.getPort()));

                ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(proxyAddGameServerPacket.getName(), new InetSocketAddress(proxyAddGameServerPacket.getHostName(), proxyAddGameServerPacket.getPort()), null, false);
                ProxyServer.getInstance().getServers().put(proxyAddGameServerPacket.getName(), serverInfo);

                if (this.plugin.getProxyConfig().isNotify().equals(true)) {
                    ProxyServer.getInstance().getPlayers().forEach((players) -> {
                        if (players.hasPermission(this.plugin.getProxyConfig().getNotifyPermission())) {
                            players.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + this.plugin.getProxyConfig().getNotifyServiceStart().replace("%service%", proxyAddGameServerPacket.getName())));
                        }
                    });
                }

            } else if (packet.getKey().equals(PacketType.ProxyRemoveGameServerPacket.name())) {
                ProxyRemoveGameServerPacket gameServerPacket = (ProxyRemoveGameServerPacket) packet.getObject();

                this.plugin.getProxy().getPluginManager().callEvent(new GameServerStopEvent(gameServerPacket.getName()));

                ProxyServer.getInstance().getServers().remove(gameServerPacket.getName());
                if (this.plugin.getProxyConfig().isNotify().equals(true)) {
                    ProxyServer.getInstance().getPlayers().forEach((players) -> {
                        if (players.hasPermission(this.plugin.getProxyConfig().getNotifyPermission())) {
                            players.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + this.plugin.getProxyConfig().getNotifyServiceStop().replace("%service%", gameServerPacket.getName())));
                        }
                    });
                }
            } else if (packet.getKey().equals(PacketType.PlayerLoginPacket.name())) {
                PlayerLoginPacket loginPacket = (PlayerLoginPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().put(loginPacket.getUniqueId(), loginPacket.getName());
            } else if (packet.getKey().equals(PacketType.PlayerLogOutPacket.name())) {
                PlayerLogOutPacket playerLogOutPacket = (PlayerLogOutPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().remove(playerLogOutPacket.getUniqueId());
            } else if (packet.getKey().equals(PacketType.GameServerStartPacket.name())) {
                GameServerStartPacket gameServerStartPacket = (GameServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getOnlineServers().add(gameServerStartPacket.getName());

                if (gameServerStartPacket.isFallBack()) {

                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(gameServerStartPacket.getName());

                    if (gameServerStartPacket.getGroup().contains("silent")) {
                        this.plugin.getSilent().put(gameServerStartPacket.getName(), serverInfo);
                    } else if (gameServerStartPacket.getGroup().contains("premium")) {
                        this.plugin.getPremiums().put(gameServerStartPacket.getName(), serverInfo);
                    } else if (gameServerStartPacket.getGroup().contains("lobby")) {
                        this.plugin.getLobbies().put(gameServerStartPacket.getName(), serverInfo);
                    }

                }

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

        }).start();
    }
}

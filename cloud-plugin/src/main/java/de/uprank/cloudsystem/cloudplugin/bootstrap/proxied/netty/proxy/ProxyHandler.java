package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.proxy;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.*;
import de.uprank.cloud.packets.type.proxy.register.ProxyRegisteredPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.proxy.sync.ProxySyncServersPacket;
import de.uprank.cloud.packets.type.sync.GetProxySyncPacket;
import de.uprank.cloud.packets.type.sync.ProxySyncPacket;
import de.uprank.cloudsystem.cloudplugin.api.proxyserver.AbstractProxyServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ProxyHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public ProxyHandler(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setProxyChannel(channelHandlerContext.channel());

        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerStartPacket.name(), new ProxyServerStartPacket(this.plugin.getCloudCore().getName(),
                this.plugin.getCloudCore().getHostName(), this.plugin.getCloudCore().getPort(), this.plugin.getCloudCore().getWrapper(),
                this.plugin.getCloudCore().getServergroup(), this.plugin.getCloudCore().getTemplate(), this.plugin.getCloudCore().getMinMemory(),
                this.plugin.getCloudCore().getMaxMemory(), ServerUtil.READY, true, this.plugin.getCloudCore().getIsDynamic())));
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyRegisteredPacket.name(), new ProxyRegisteredPacket(this.plugin.getCloudCore().getName())));

        channelHandlerContext.writeAndFlush(new Packet(PacketType.GetProxySyncPacket.name(), new GetProxySyncPacket(this.plugin.getCloudCore().getName())));

        Map<String, Object> data = new HashMap<>();
        data.put("hostName", this.plugin.getCloudCore().getHostName());
        data.put("port", this.plugin.getCloudCore().getPort());
        data.put("wrapper", this.plugin.getCloudCore().getWrapper());

        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.ProxyServerUpdatePacket.name(), new ProxyServerUpdatePacket(this.plugin.getCloudCore().getName(), data)));

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.ProxySyncServersPacket.name())) {
                ProxySyncServersPacket proxySyncServersPacket = (ProxySyncServersPacket) packet.getObject();

                System.out.println(proxySyncServersPacket.getName() + ":" + proxySyncServersPacket.getServers().getSecond() + ":" + proxySyncServersPacket.getServers().getThird());

                ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(proxySyncServersPacket.getServers().getFirst(), new InetSocketAddress(proxySyncServersPacket.getServers().getSecond(), proxySyncServersPacket.getServers().getThird()), null, false);
                ProxyServer.getInstance().getServers().put(proxySyncServersPacket.getName(), serverInfo);
            } else if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {
                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getProxyServerManager().getProxyServer().add(new AbstractProxyServer(proxyServerStartPacket.getName()));
                this.plugin.getProxies().add(proxyServerStartPacket.getName());


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

                ProxyServer.getInstance().getServers().remove(gameServerPacket.getName());
                if (this.plugin.getProxyConfig().isNotify().equals(true)) {
                    ProxyServer.getInstance().getPlayers().forEach((players) -> {
                        if (players.hasPermission(this.plugin.getProxyConfig().getNotifyPermission())) {
                            players.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + this.plugin.getProxyConfig().getNotifyServiceStop().replace("%service%", gameServerPacket.getName())));
                        }
                    });
                }
            } else if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {
                ProxyServerStopPacket proxyServerStopPacket = (ProxyServerStopPacket) packet.getObject();

                this.plugin.getCloudCore().getProxyServerManager().getProxyServer().remove(proxyServerStopPacket.getName());
                this.plugin.getProxies().remove(proxyServerStopPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyToggleMaintenancePacket.name())) {
                ProxyToggleMaintenancePacket proxyToggleMaintenancePacket = (ProxyToggleMaintenancePacket) packet.getObject();


                if (proxyToggleMaintenancePacket.getMaintenance().equals(true)) {

                    this.plugin.getProxy().getPlayers().forEach((proxiedPlayer) -> {

                        if (!(proxiedPlayer.hasPermission("de.uprank.cloud.maintenance.bypass"))) {

                            proxiedPlayer.disconnect("§cMaintenance! :)");

                        }
                    });
                }

            } else if (packet.getKey().equals(PacketType.ProxySyncPacket.name())) {
                ProxySyncPacket proxySyncPacket = (ProxySyncPacket) packet.getObject();

                if (this.plugin.getProxy().getServers().containsKey(proxySyncPacket.getServerName())) {
                    return;
                }

                ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(proxySyncPacket.getServerName(), new InetSocketAddress(proxySyncPacket.getAddress(), proxySyncPacket.getPort()), null, false);
                ProxyServer.getInstance().getServers().put(proxySyncPacket.getServerName(), serverInfo);

            } else if (packet.getKey().equals(PacketType.CloudStopPacket.name())) {
                this.plugin.getProxy().stop();
            }

        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }
}

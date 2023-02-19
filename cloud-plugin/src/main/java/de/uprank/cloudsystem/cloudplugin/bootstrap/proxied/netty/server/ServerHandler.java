package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.server;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloudsystem.cloudplugin.api.gameserver.AbstractGameServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public ServerHandler(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setServerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            System.out.println("[!!!]" + packet.getKey());

            if (packet.getKey().equals(PacketType.GameServerStartPacket.name())) {
                GameServerStartPacket gameServerStartPacket = (GameServerStartPacket) packet.getObject();

                this.plugin.getCloudCore().getGameServerManager().getGameServer().add(new AbstractGameServer(gameServerStartPacket.getName(), gameServerStartPacket.getReplayId(), 0, 0, null, null));
                this.plugin.getServers().add(gameServerStartPacket.getName());

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

                this.plugin.getCloudCore().getGameServerManager().getGameServer().remove(gameServerStopPacket.getName());
                this.plugin.getProxyChannel().writeAndFlush(new Packet(PacketType.ProxyRemoveGameServerPacket.name(), new ProxyRemoveGameServerPacket(gameServerStopPacket.getName())));
                this.plugin.getServers().remove(gameServerStopPacket.getName());

            }

        }).start();

    }

}

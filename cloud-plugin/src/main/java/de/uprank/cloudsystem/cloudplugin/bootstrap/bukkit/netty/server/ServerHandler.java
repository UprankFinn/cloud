package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.server;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.server.*;
import de.uprank.cloud.packets.type.sign.SignPacket;
import de.uprank.cloudsystem.cloudapi.events.bukkit.server.BukkitGameServerRegisterEvent;
import de.uprank.cloudsystem.cloudapi.events.bukkit.server.BukkitGameServerUnregisterEvent;
import de.uprank.cloudsystem.cloudplugin.api.gameserver.AbstractGameServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudBukkitPlugin plugin;

    public ServerHandler(CloudBukkitPlugin plugin) {
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

            System.out.println("[SERVERCHANNEL] " + packet.getKey());

            if (packet.getKey().equals(PacketType.GameServerStartPacket.name())) {
                GameServerStartPacket gameServerStartPacket = (GameServerStartPacket) packet.getObject();

                /*
                BukkitEvent
                 */

                Bukkit.getServer().getPluginManager().callEvent(new BukkitGameServerRegisterEvent(
                        gameServerStartPacket.getName(), gameServerStartPacket.getReplayId(),
                        gameServerStartPacket.getHostName(), gameServerStartPacket.getPort(),
                        gameServerStartPacket.getWrapper(), gameServerStartPacket.getGroup(),
                        gameServerStartPacket.getTemplate()));

            } else if (packet.getKey().equals(PacketType.GameServerSignPacket.name())) {
                SignPacket signPacket = (SignPacket) packet.getObject();


            } else if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();

                /*
                BukkitEvent
                 */

                Bukkit.getServer().getPluginManager().callEvent(new BukkitGameServerUnregisterEvent(
                        gameServerStopPacket.getName(), gameServerStopPacket.getHostName(), gameServerStopPacket.getPort(),
                        gameServerStopPacket.getWrapper(), gameServerStopPacket.getGroup(), gameServerStopPacket.getTemplate()
                ));

                if (gameServerStopPacket.getName().equals(this.plugin.getCloudCore().getName())) {
                    Bukkit.shutdown();
                }
            } else if (packet.getKey().equals(PacketType.GameServerSyncPacket.name())) {
                GameServerSyncPacket gameServerSyncPacket = (GameServerSyncPacket) packet.getObject();

                this.plugin.getCloudCore().getAbstractGameServerManager().getGameServers().put(gameServerSyncPacket.getName(), new AbstractGameServer(gameServerSyncPacket.getName(), this.plugin.getCloudCore().getGameId(), "null", ServerUtil.LOBBY));

                Bukkit.broadcastMessage("#1");
                System.out.println("added Server " + gameServerSyncPacket.getName());

            } else if (packet.getKey().equals(PacketType.GameServerUnsyncPacket.name())) {
                GameServerUnsyncPacket gameServerUnsyncPacket = (GameServerUnsyncPacket) packet.getObject();

                this.plugin.getCloudCore().getAbstractGameServerManager().getGameServers().remove(gameServerUnsyncPacket.getName());
                Bukkit.broadcastMessage("#1");
                System.out.println("removed Server " + gameServerUnsyncPacket.getName());

            } else if (packet.getKey().equals(PacketType.GameServerUpdatePacket.name())) {
                GameServerUpdatePacket gameServerUpdatePacket = (GameServerUpdatePacket) packet.getObject();

                if (gameServerUpdatePacket.getUpdatedData().containsKey("onlinePlayers")) {
                    this.plugin.getCloudCore().getAbstractGameServerManager().getGameServer(gameServerUpdatePacket.getServerName()).setPlayerAmount((Integer) gameServerUpdatePacket.getUpdatedData().get("onlinePlayers"));
                } else if (gameServerUpdatePacket.getUpdatedData().containsKey("serverState")) {
                    this.plugin.getCloudCore().getAbstractGameServerManager().getGameServer(gameServerUpdatePacket.getServerName()).setServerUtil((ServerUtil) gameServerUpdatePacket.getUpdatedData().get("serverState"));
                } else if (gameServerUpdatePacket.getUpdatedData().containsKey("maxPlayers")) {
                    this.plugin.getCloudCore().getAbstractGameServerManager().getGameServer(gameServerUpdatePacket.getServerName()).setMaxPlayerAmount((Integer) gameServerUpdatePacket.getUpdatedData().get("maxPlayers"));
                } else if (gameServerUpdatePacket.getUpdatedData().containsKey("motd")) {
                    this.plugin.getCloudCore().getAbstractGameServerManager().getGameServer(gameServerUpdatePacket.getServerName()).setMotd((String) gameServerUpdatePacket.getUpdatedData().get("motd"));
                }


            } else if (packet.getKey().equals(PacketType.CloudStopPacket.name())) {
                Bukkit.shutdown();
            }

        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }
}

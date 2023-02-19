package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.server;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloud.packets.type.sign.SignPacket;
import de.uprank.cloud.packets.util.StopReason;
import de.uprank.cloudsystem.cloudplugin.api.gameserver.AbstractGameServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

                this.plugin.getCloudCore().getGameServerManager().getGameServer().add(new AbstractGameServer(gameServerStartPacket.getName(), gameServerStartPacket.getReplayId(), 0, 0, null, null));

            } else if (packet.getKey().equals(PacketType.GameServerSignPacket.name())) {
                SignPacket signPacket = (SignPacket) packet.getObject();


            } else if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();

                this.plugin.getCloudCore().getGameServerManager().getGameServer().remove(gameServerStopPacket.getName());
                if (gameServerStopPacket.getName().equals(this.plugin.getCloudCore().getName())) {
                    Bukkit.shutdown();
                }

            }

        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }
}

package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.player;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudBukkitPlugin plugin;

    public PlayerHandler(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setPlayerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            System.out.println(packet.getKey());

            if (packet.getKey().equals(PacketType.PlayerLoginPacket.name())) {
                PlayerLoginPacket loginPacket = (PlayerLoginPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().put(loginPacket.getUniqueId(), loginPacket.getName());
            } else if (packet.getKey().equals(PacketType.PlayerLogOutPacket.name())) {
                PlayerLogOutPacket playerLogOutPacket = (PlayerLogOutPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().remove(playerLogOutPacket.getUniqueId());

            }

        }).start();

    }
}

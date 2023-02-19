package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.player;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloud.packets.type.player.PlayerMessagePacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public PlayerHandler(CloudProxiedPlugin plugin) {
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

            if (packet.getKey().equals(PacketType.PlayerMessagePacket.name())) {
                PlayerMessagePacket playerMessagePacket = (PlayerMessagePacket) packet.getObject();

                if (this.plugin.getProxy().getPlayer(playerMessagePacket.getUniqueId()) != null) {
                    this.plugin.getProxy().getPlayer(playerMessagePacket.getUniqueId()).sendMessage(new TextComponent(playerMessagePacket.getMessage()));
                }

            } else if (packet.getKey().equals(PacketType.PlayerLoginPacket.name())) {
                PlayerLoginPacket loginPacket = (PlayerLoginPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().put(loginPacket.getUniqueId(), loginPacket.getName());
            } else if (packet.getKey().equals(PacketType.PlayerLogOutPacket.name())) {
                PlayerLogOutPacket playerLogOutPacket = (PlayerLogOutPacket) packet.getObject();
                this.plugin.getCloudCore().getOnlinePlayers().remove(playerLogOutPacket.getUniqueId());
            }

        }).start();

    }
}

package de.uprank.cloud.module.master.netty.player;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloud.packets.type.player.PlayerSwitchServerPacket;
import de.uprank.cloud.util.PlayerUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerHandler extends SimpleChannelInboundHandler<Object> {

    private final MasterModule masterModule;

    public PlayerHandler(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.setPlayerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.PlayerLoginPacket.name())) {

                PlayerLoginPacket playerLoginPacket = (PlayerLoginPacket) packet.getObject();
                this.masterModule.info("The Player &b" + playerLoginPacket.getName() + " is now logging in.");
                PlayerUtil.addPlayer(playerLoginPacket.getUniqueId(), playerLoginPacket.getName());

                this.masterModule.getProxyManager().getProxyChannels().forEach((s, proxy) -> proxy.getChannel().writeAndFlush(packet));

            } else if (packet.getKey().equals(PacketType.PlayerSwitchServerPacket.name())) {

                PlayerSwitchServerPacket playerSwitchServerPacket = (PlayerSwitchServerPacket) packet.getObject();

                this.masterModule.getUserOnProxy().put(playerSwitchServerPacket.getUniqueId(), playerSwitchServerPacket.getProxy());

                this.masterModule.getUserOnServer().remove(playerSwitchServerPacket.getUniqueId());
                this.masterModule.getUserOnServer().put(playerSwitchServerPacket.getUniqueId(), playerSwitchServerPacket.getServer());

                this.masterModule.info("switched server " + playerSwitchServerPacket.getUniqueId().toString() + " " + playerSwitchServerPacket.getServer() + ":" + playerSwitchServerPacket.getProxy());

            } else if (packet.getKey().equals(PacketType.PlayerLogOutPacket.name())) {

                PlayerLogOutPacket playerLogOutPacket = (PlayerLogOutPacket) packet.getObject();
                this.masterModule.info("The Player &b" + playerLogOutPacket.getName() + " is now logging out. (from " + this.masterModule.getUserOnProxy().get(playerLogOutPacket.getUniqueId()) + ":" + this.masterModule.getUserOnServer().get(playerLogOutPacket.getUniqueId() + ")"));
                PlayerUtil.removePlayer(playerLogOutPacket.getUniqueId());

                this.masterModule.getUserOnProxy().remove(playerLogOutPacket.getUniqueId());
                this.masterModule.getUserOnServer().remove(playerLogOutPacket.getUniqueId());

                this.masterModule.getProxyManager().getProxyChannels().forEach((s, proxy) -> proxy.getChannel().writeAndFlush(packet));

            }

        }).start();
    }
}

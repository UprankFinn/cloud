package de.uprank.cloud.module.wrapper.netty.server;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private final WrapperModule wrapperModule;

    public ServerHandler(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.wrapperModule.setServerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            this.wrapperModule.info("[Server] " + packet.getKey());

            if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();
                this.wrapperModule.info(gameServerStopPacket.getName() + ":" + gameServerStopPacket.getHostName() + ":" + gameServerStopPacket.getPort() + ":" + gameServerStopPacket.getWrapper());

                if (gameServerStopPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getServerManager().getServer(gameServerStopPacket.getName()).shutdown();
                    this.wrapperModule.getServerManager().getServers().remove(gameServerStopPacket.getName());
                }

            } else if (packet.getKey().equals(PacketType.GameServerRequestPacket.name())) {
                GameServerRequestPacket gameServerRequestPacket = (GameServerRequestPacket) packet.getObject();


                if (gameServerRequestPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getServerManager().startService(gameServerRequestPacket.getGroup(), gameServerRequestPacket.getTemplate(), String.valueOf(UUID.randomUUID().toString().split("-")[0]), gameServerRequestPacket.getMinMemory(), gameServerRequestPacket.getMaxMemory(), gameServerRequestPacket.isFallBack(), gameServerRequestPacket.isDynamic(), 1);
                }

            }

        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.wrapperModule.getServerManager().getServers().forEach((s, server) -> server.shutdown());
    }
}

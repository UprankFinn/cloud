package de.uprank.cloud.module.master.netty.server;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.servers.Server;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloud.packets.type.sync.ProxySyncPacket;
import de.uprank.cloud.packets.util.StopReason;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private final MasterModule masterModule;

    public ServerHandler(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.setServerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            this.masterModule.info("[Server]" + packet.getKey());

            if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();

                if (this.masterModule.getServerManager().getServers().containsKey(gameServerStopPacket.getName())) {
                    this.masterModule.getServerManager().getServers().remove(gameServerStopPacket.getName());
                }

            } else if (packet.getKey().equals(PacketType.GameServerStartPacket.name())) {
                GameServerStartPacket gameServerStartPacket = (GameServerStartPacket) packet.getObject();

                if (!(this.masterModule.getServerManager().getServers().containsKey(gameServerStartPacket.getName()))) {
                    this.masterModule.getServerManager().getServers().put(gameServerStartPacket.getName(), new Server(gameServerStartPacket.getName(), gameServerStartPacket.getHostName(), gameServerStartPacket.getPort(), gameServerStartPacket.isFallBack(), gameServerStartPacket.getMinMemory(), gameServerStartPacket.getMaxMemory(), gameServerStartPacket.isDynamic(), channelHandlerContext.channel()));
                    this.masterModule.info("[ONLINE] SERVER IS NOW STARTING and added to ServersArray " + gameServerStartPacket.getName());
                }

                this.masterModule.getCloudSignManager().getCloudSigns().forEach((uuid, cloudSign) -> {
                    //todo:SIGN
                });

                this.masterModule.getProxyManager().getProxies().forEach((s, proxy) -> {
                    this.masterModule.getServerManager().getServers().forEach((s1, server) -> {
                        proxy.getChannel().writeAndFlush(new Packet(PacketType.ProxySyncPacket.name(), new ProxySyncPacket(proxy.getName(), server.getName(), server.getHostName(), server.getPort())));
                    });
                });

            } else if (packet.getKey().equals(PacketType.GameServerUpdatePacket.name())) {

                GameServerUpdatePacket gameServerUpdatePacket = (GameServerUpdatePacket) packet.getObject();

                this.masterModule.info("Updated ServerData from &e" + gameServerUpdatePacket.getServerName());
                gameServerUpdatePacket.getUpdatedData().forEach((s, o) -> this.masterModule.info("Updated-DATA: " + s + " -> " + o.toString()));

                if (this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()) != null) {
                    if (gameServerUpdatePacket.getUpdatedData().containsKey("onlinePlayers")) {
                        Integer currentlyPlayers = (Integer) gameServerUpdatePacket.getUpdatedData().get("onlinePlayers");

                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setPlayers(currentlyPlayers);


                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("maxPlayers")) {
                        Integer maxPlayers = (Integer) gameServerUpdatePacket.getUpdatedData().get("maxPlayers");
                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setMaxPlayers(maxPlayers);
                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("motd")) {
                        String motd = (String) gameServerUpdatePacket.getUpdatedData().get("motd");
                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setMotd(motd);
                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("group")) {
                        String group = (String) gameServerUpdatePacket.getUpdatedData().get("group");
                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setGroup(group);
                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("template")) {
                        String template = (String) gameServerUpdatePacket.getUpdatedData().get("template");
                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setTemplate(template);
                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("wrapper")) {
                        String wrapper = (String) gameServerUpdatePacket.getUpdatedData().get("wrapper");
                        this.masterModule.getServerManager().getServer(gameServerUpdatePacket.getServerName()).setWrapper(wrapper);
                    } else if (gameServerUpdatePacket.getUpdatedData().containsKey("serverState")) {
                        String serverState = (String) gameServerUpdatePacket.getUpdatedData().get("serverState");

                    }
                }

            } else if (packet.getKey().equals(PacketType.GameServerRequestPacket.name())) {
                GameServerRequestPacket gameServerRequestPacket = (GameServerRequestPacket) packet.getObject();
                this.masterModule.info("received #1 " + gameServerRequestPacket.getGroup());

                this.masterModule.getWrapperManager().getWrapperbyName(gameServerRequestPacket.getWrapper()).sendPacket(new Packet(PacketType.GameServerRequestStopPacket.name(), gameServerRequestPacket));

            }

        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.getServerManager().getServers().forEach((s, server) -> channelHandlerContext.writeAndFlush(new Packet(PacketType.GameServerStopPacket.name(), new GameServerStopPacket(server.getName(), StopReason.Cloud_STOP, server.getGroup(), server.getTemplate(), server.getWrapper(), server.getHostName(), server.getPort(), server.getMinMemory(), server.getMaxMemory(), false, server.getFallBack(), server.getDynamic()))));
    }
}

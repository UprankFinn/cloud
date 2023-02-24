package de.uprank.cloud.module.master.netty.proxy;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.proxies.Proxy;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.CloudStopPacket;
import de.uprank.cloud.packets.type.player.PlayerMessagePacket;
import de.uprank.cloud.packets.type.proxy.*;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.sync.GetProxySyncPacket;
import de.uprank.cloud.packets.type.sync.ProxySyncPacket;
import de.uprank.cloud.packets.util.StopReason;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProxyHandler extends SimpleChannelInboundHandler<Object> {

    private final MasterModule masterModule;

    public ProxyHandler(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.setProxyChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {
                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxyChannels().put(proxyServerStartPacket.getName(), new Proxy(proxyServerStartPacket.getName(), channelHandlerContext.channel()));

                this.masterModule.getServerManager().getServers().forEach((s, server) -> this.masterModule.getProxyManager().getProxyChannels().forEach((s1, proxy) -> {
                    if (proxy.getChannel() == null) {
                        this.masterModule.info("asdassreurtjurtjur");
                    }
                    proxy.getChannel().writeAndFlush(new Packet(PacketType.ProxyAddGameServerPacket.name(), new ProxyAddGameServerPacket(server.getName(), server.getHostName(), server.getPort())));
                    this.masterModule.info("&aSync Proxy with Server: " + proxy.getName() + ":" + server.getName());
                }));

                Proxy proxys = this.masterModule.getProxyManager().getProxyChannels().get(proxyServerStartPacket.getName());
                proxys.setGroup(proxyServerStartPacket.getGroup());
                proxys.setTemplate(proxyServerStartPacket.getTemplate());
                proxys.setDynamic(proxyServerStartPacket.getDynamic());
                proxys.setMinMemory(proxyServerStartPacket.getMinMemory());
                proxys.setMaxMemory(proxyServerStartPacket.getMaxMemory());

                this.masterModule.getProxyManager().getProxyChannels().forEach((s, proxy) -> proxy.getChannel().writeAndFlush(packet));

            } else if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {

                ProxyServerStopPacket proxyServerStopPacket = (ProxyServerStopPacket) packet.getObject();

                this.masterModule.getWrapperManager().getWrapperList().forEach((wrappers) -> {
                    wrappers.sendPacket(packet);
                });

                this.masterModule.getProxyManager().getProxyChannels().remove(proxyServerStopPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyAddGameServerPacket.name())) {

                ProxyAddGameServerPacket gameServerStartPacket = (ProxyAddGameServerPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxyChannels().forEach(((s, proxy) -> proxy.getChannel().writeAndFlush(packet)));

            } else if (packet.getKey().equals(PacketType.ProxyRemoveGameServerPacket.name())) {

                ProxyRemoveGameServerPacket proxyAddGameServerPacket = (ProxyRemoveGameServerPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxyChannels().forEach(((s, proxy) -> proxy.getChannel().writeAndFlush(packet)));

            } else if (packet.getKey().equals(PacketType.ProxyServerRequestPacket.name())) {
                ProxyServerRequestPacket proxyServerRequestPacket = (ProxyServerRequestPacket) packet.getObject();
                if (this.masterModule.getWrapperManager().getWrapperbyName(proxyServerRequestPacket.getWrapper()) != null)
                    this.masterModule.getWrapperManager().getWrapperbyName(proxyServerRequestPacket.getWrapper()).sendPacket(packet);
            } else if (packet.getKey().equals(PacketType.ProxyToggleMaintenancePacket.name())) {
                ProxyToggleMaintenancePacket proxyToggleMaintenancePacket = (ProxyToggleMaintenancePacket) packet.getObject();

                this.masterModule.getProxyManager().getProxies().forEach((s, proxy) -> proxy.getChannel().writeAndFlush(packet));
                this.masterModule.info("&cThe maintenance was " + proxyToggleMaintenancePacket.getMaintenance());

            } else if (packet.getKey().equals(PacketType.ProxyServerUpdatePacket.name())) {
                ProxyServerUpdatePacket proxyServerUpdatePacket = (ProxyServerUpdatePacket) packet.getObject();

                if (this.masterModule.getProxyManager().getProxy(proxyServerUpdatePacket.getProxyName()) != null) {
                    if (proxyServerUpdatePacket.getUpdatedData().containsKey("hostName")) {
                        String hostName = (String) proxyServerUpdatePacket.getUpdatedData().get("hostName");
                        this.masterModule.getProxyManager().getProxy(proxyServerUpdatePacket.getProxyName()).setHostName(hostName);
                    } else if (proxyServerUpdatePacket.getUpdatedData().containsKey("port")) {
                        Integer port = (Integer) proxyServerUpdatePacket.getUpdatedData().get("port");
                        this.masterModule.getProxyManager().getProxy(proxyServerUpdatePacket.getProxyName()).setPort(port);
                    } else if (proxyServerUpdatePacket.getUpdatedData().containsKey("wrapper")) {
                        String wrapper = (String) proxyServerUpdatePacket.getUpdatedData().get("wrapper");
                        this.masterModule.getProxyManager().getProxy(proxyServerUpdatePacket.getProxyName()).setWrapper(wrapper);
                    }
                }
            } else if (packet.getKey().equals(PacketType.PlayerMessagePacket.name())) {
                PlayerMessagePacket playerMessagePacket = (PlayerMessagePacket) packet.getObject();

                this.masterModule.getProxyManager().getProxyChannels().forEach(((s, proxy) -> proxy.getChannel().writeAndFlush(packet)));

            } else if (packet.getKey().equals(PacketType.GetProxySyncPacket.name())) {
                GetProxySyncPacket getProxySyncPacket = (GetProxySyncPacket) packet.getObject();

                this.masterModule.getServerManager().getServers().forEach((s1, server) -> {
                    this.masterModule.getProxyChannel().writeAndFlush(new Packet(PacketType.ProxySyncPacket.name(), new ProxySyncPacket(getProxySyncPacket.getProxyName(), server.getName(), server.getHostName(), server.getPort())));
                });

            }

        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.getProxyManager().getProxies().forEach((s, proxy) -> {
            channelHandlerContext.writeAndFlush(new Packet(PacketType.CloudStopPacket.name(), new CloudStopPacket()));
        });
    }
}

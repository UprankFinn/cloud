package de.uprank.cloud.module.wrapper.netty.proxy;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyServerRequestPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class ProxyHandler extends SimpleChannelInboundHandler<Object> {

    private final WrapperModule wrapperModule;

    public ProxyHandler(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.wrapperModule.setProxyChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {
                ProxyServerStopPacket proxyServerStopPacket = (ProxyServerStopPacket) packet.getObject();

                if (proxyServerStopPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getProxyManager().getProxyByName(proxyServerStopPacket.getName()).shutdown();
                    this.wrapperModule.getProxyManager().getProxies().remove(proxyServerStopPacket.getName());
                }

            }  else if (packet.getKey().equals(PacketType.ProxyServerRequestPacket.name())) {
                ProxyServerRequestPacket proxyServerRequestPacket = (ProxyServerRequestPacket) packet.getObject();

                if (proxyServerRequestPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getProxyManager().startService(proxyServerRequestPacket.getGroup(), proxyServerRequestPacket.getTemplate(), String.valueOf(UUID.randomUUID().toString().split("-")[0]), proxyServerRequestPacket.getMinMemory(), proxyServerRequestPacket.getMaxMemory(), false, 1, proxyServerRequestPacket.isDynamic());
                }

            }

        }).start();

    }

}

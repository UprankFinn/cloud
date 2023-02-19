package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.wrapper;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WrapperHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudProxiedPlugin plugin;

    public WrapperHandler(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setWrapperChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;



        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.getProxy().stop();
    }
}

package de.uprank.cloud.module.wrapper.netty.player;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerHandler extends SimpleChannelInboundHandler<Object> {

    private final WrapperModule wrapperModule;

    public PlayerHandler(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.wrapperModule.setPlayerChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;


        }).start();

    }
}

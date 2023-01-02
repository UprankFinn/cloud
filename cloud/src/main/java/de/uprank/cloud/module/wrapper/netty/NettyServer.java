package de.uprank.cloud.module.wrapper.netty;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Getter;
import lombok.SneakyThrows;

public class NettyServer implements Runnable {

    private final WrapperModule wrapperModule;

    @Getter
    private Channel channel;

    private final String hostName;
    private final Integer port;

    public NettyServer(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
        this.hostName = "127.0.0.1";
        this.port = 1234;
    }

    @Override
    @SneakyThrows
    public void run() {

        //SslContext sslCtx = SslContextBuilder.forClient().trustManager(FileUtils.getFile("unestia.net.key")).build();

        EventLoopGroup eventLoopGroup = NettyUtil.getEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NettyUtil.getSocketChannel())
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                            //socketChannel.pipeline().addLast(sslCtx.newHandler(socketChannel.alloc(), hostName, port));
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new NettyHandler(wrapperModule));

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(this.hostName, this.port).sync();
            this.channel = channelFuture.channel();

            ChannelFuture closeFuture = this.channel.closeFuture();
            closeFuture.addListener(future -> {

                this.wrapperModule.warning("No connection to CloudSystem! Reconnect in 2 Seconds!");
                Thread.sleep(2000);

                eventLoopGroup.shutdownGracefully();
            });
            closeFuture.sync();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

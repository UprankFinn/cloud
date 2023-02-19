package de.uprank.cloud.module.wrapper.netty.player;

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

public class PlayerHandlerClient implements Runnable {

    private final WrapperModule wrapperModule;

    @Getter
    private Channel channel;

    private final String hostName;
    private final Integer port;

    public PlayerHandlerClient(WrapperModule wrapperModule, String hostName, Integer port) {
        this.wrapperModule = wrapperModule;

        this.hostName = hostName;
        this.port = port;
    }

    @Override
    @SneakyThrows
    public void run() {

        EventLoopGroup eventLoopGroup = NettyUtil.getEventLoopGroup();

        //SSLHandlerProvider.initSSLContext();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NettyUtil.getSocketChannel())
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //SslHandler sslHandler = SSLHandlerProvider.getSSLHandler();

                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //channelPipeline.addLast(sslHandler);
                            //channelPipeline.addLast(new HttpServerCodec());
                            //channelPipeline.addLast(new HttpObjectAggregator(1048576));
                            channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                            channelPipeline.addLast(new ObjectEncoder());
                            channelPipeline.addLast(new PlayerHandler(wrapperModule));

                            /*new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                                    channelHandlerContext.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
                                    channelHandlerContext.channel().close();
                                }
                            };*/

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

package de.uprank.cloud.module.master.netty;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.packets.netty.SSLHandlerProvider;
import de.uprank.cloud.util.NettyUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslHandler;
import lombok.SneakyThrows;

public class NettyServer implements Runnable {

    private final MasterModule masterModule;

    private final Integer port;

    public NettyServer(MasterModule masterModule) {
        this.masterModule = masterModule;
        this.port = 1234;
    }

    @Override
    @SneakyThrows
    public void run() {

        EventLoopGroup bossGroup = NettyUtil.getEventLoopGroup();
        EventLoopGroup workerGroup = NettyUtil.getEventLoopGroup();

        //SSLHandlerProvider.initSSLContext();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NettyUtil.getServerSocketChannel())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            //SslHandler sslHandler = SSLHandlerProvider.getSSLHandler();

                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //channelPipeline.addLast(sslHandler);
                            //channelPipeline.addLast(new HttpServerCodec());
                            //channelPipeline.addLast(new HttpObjectAggregator(1048576));
                            channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                            channelPipeline.addLast(new ObjectEncoder());
                            channelPipeline.addLast(new NettyHandler(masterModule));

                            /*new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                                    channelHandlerContext.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
                                    channelHandlerContext.channel().close();
                                }
                            };*/

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, Integer.MAX_VALUE);
            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

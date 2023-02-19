package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.server;

import de.uprank.cloud.packets.netty.NettyUtil;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Getter;
import lombok.SneakyThrows;

public class ServerHandlerClient implements Runnable {

    private final CloudProxiedPlugin plugin;

    @Getter
    private Channel channel;

    private final String hostName;
    private final Integer port;

    public ServerHandlerClient(CloudProxiedPlugin plugin, String hostName, Integer port) {
        this.plugin = plugin;

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
                            channelPipeline.addLast(new ServerHandler(plugin));

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

                System.out.println("No connection to CloudSystem! Reconnect in 2 Seconds!");
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

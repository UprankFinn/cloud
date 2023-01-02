package de.uprank.cloud.module.master.netty;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.util.NettyUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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

        //SslContext sslContext = SslContextBuilder.forServer(FileUtils.getFile("unestia.net.csr"), FileUtils.getFile("unestia.net.key")).build();


        EventLoopGroup bossGroup = NettyUtil.getEventLoopGroup();
        EventLoopGroup workerGroup = NettyUtil.getEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NettyUtil.getServerSocketChannel())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //channelPipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
                            channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                            channelPipeline.addLast(new ObjectEncoder());
                            channelPipeline.addLast(new NettyHandler(masterModule));
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

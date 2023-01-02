package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty;

import de.uprank.cloud.packets.NettyUtil;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Getter;
import lombok.SneakyThrows;

public class NettyServer implements Runnable {

    private final CloudBukkitPlugin plugin;

    private final String hostName;
    private final Integer port;

    @Getter
    private Channel channel;

    public NettyServer(CloudBukkitPlugin plugin) {
        this.plugin = plugin;

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
                            socketChannel.pipeline().addLast(new NettyHandler(plugin));

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

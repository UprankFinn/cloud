package de.uprank.cloud;

public class Master {

    /*SslContext sslCtx = SslContextBuilder.forServer(certificateChainFile, privateKeyFile)
            .build();

    ServerBootstrap b = new ServerBootstrap();
b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
  .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(sslCtx.newHandler(ch.alloc()));
            p.addLast(new EchoServerHandler());
        }
    });

b.bind(PORT).sync().channel().closeFuture().sync();*/


}

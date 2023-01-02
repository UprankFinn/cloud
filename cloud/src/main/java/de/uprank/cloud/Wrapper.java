package de.uprank.cloud;

public class Wrapper {

    /*SslContext sslCtx = SslContextBuilder.forClient()
            .trustManager(new File(trustStoreFile))
            .build();

    Bootstrap b = new Bootstrap();
b.group(workerGroup)
            .channel(NioSocketChannel.class)
  .handler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
            p.addLast(new EchoClientHandler());
        }
    });

b.connect(HOST, PORT).sync().channel().closeFuture().sync();*/


}

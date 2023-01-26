package de.uprank.cloud.packets.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUtil {

    private static final Boolean EPOLL = Epoll.isAvailable();
    private static final Boolean KQUEUE = KQueue.isAvailable();

    public static EventLoopGroup getEventLoopGroup() {
        return ((boolean)NettyUtil.EPOLL) ? new EpollEventLoopGroup() : (((boolean)NettyUtil.KQUEUE) ? new KQueueEventLoopGroup() : new NioEventLoopGroup());
    }

    public static Class<? extends SocketChannel> getSocketChannel() {
        return (Class<? extends SocketChannel>)(((boolean)NettyUtil.EPOLL) ? EpollSocketChannel.class : (((boolean)NettyUtil.KQUEUE) ? KQueueSocketChannel.class : NioSocketChannel.class));
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannel() {
        return (Class<? extends ServerSocketChannel>)(((boolean)NettyUtil.EPOLL) ? EpollServerSocketChannel.class : (((boolean)NettyUtil.KQUEUE) ? KQueueServerSocketChannel.class : NioServerSocketChannel.class));
    }

}

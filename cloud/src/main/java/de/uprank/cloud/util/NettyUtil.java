package de.uprank.cloud.util;

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

    public static boolean EPOLL;
    public static boolean KQUEUE;

    public NettyUtil() {
        EPOLL = Epoll.isAvailable();
        KQUEUE = KQueue.isAvailable();
    }

    public static Class<? extends SocketChannel> getSocketChannel() {
        return (NettyUtil.EPOLL ? EpollSocketChannel.class : (NettyUtil.KQUEUE ? KQueueSocketChannel.class : NioSocketChannel.class));
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannel() {
        return (NettyUtil.EPOLL ? EpollServerSocketChannel.class : (NettyUtil.KQUEUE ? KQueueServerSocketChannel.class : NioServerSocketChannel.class));
    }

    public static EventLoopGroup getEventLoopGroup() {
        return NettyUtil.EPOLL ? new EpollEventLoopGroup() : (NettyUtil.KQUEUE ? new KQueueEventLoopGroup() : new NioEventLoopGroup());
    }

}

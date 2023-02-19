package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.wrapper;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

public class WrapperHandler extends SimpleChannelInboundHandler<Object> {

    private final CloudBukkitPlugin plugin;

    public WrapperHandler(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.plugin.setWrapperChannel(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {
            Packet packet = (Packet) object;

            System.out.println(packet.getKey());

        }).start();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        Bukkit.shutdown();
    }
}

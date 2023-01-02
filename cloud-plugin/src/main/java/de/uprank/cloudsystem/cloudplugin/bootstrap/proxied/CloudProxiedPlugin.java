package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloudsystem.cloudplugin.CloudCore;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.commands.CloudCommand;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.config.ProxyConfig;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener.PlayerDisconnectListener;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener.ProxyPingListener;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener.ServerConnectedListener;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.NettyServer;
import io.netty.channel.Channel;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class CloudProxiedPlugin extends Plugin {

    @Getter
    private static CloudProxiedPlugin instance;

    private final CloudCore cloudCore;
    private final ProxyConfig proxyConfig;

    private Channel channel;
    private NettyServer nettyServer;

    public CloudProxiedPlugin() {
        instance = this;
        this.cloudCore = new CloudCore("service.json");
        this.proxyConfig = new ProxyConfig(this);
        this.proxyConfig.readConfig();
    }

    @Override
    public void onEnable() {


        this.nettyServer = new NettyServer(this);
        new Thread(this.nettyServer).start();

        new CloudCommand(this);

        new PlayerDisconnectListener(this);
        new ProxyPingListener(this);
        new ServerConnectedListener(this);

    }

    @Override
    public void onDisable() {

        if (this.cloudCore.getJedis().isConnected()) {
            this.getProxy().getPlayers().forEach((players) -> this.cloudCore.getJedis().lrem("cloudPlayers", 1, players.getUniqueId().toString()));
        }

        if (this.channel.isOpen() && this.channel.isActive() && this.channel.isRegistered()) {
            this.channel.close();
        }

    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

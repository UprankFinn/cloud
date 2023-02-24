package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import de.uprank.cloud.packets.util.StopReason;
import de.uprank.cloudsystem.cloudplugin.CloudCore;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.commands.CloudCommand;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.config.ProxyConfig;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener.*;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.friend.FriendHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.player.PlayerHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.proxy.ProxyHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.server.ServerHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.netty.wrapper.WrapperHandlerClient;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class CloudProxiedPlugin extends Plugin {

    @Getter
    private static CloudProxiedPlugin instance;

    private CloudCore cloudCore;
    private ProxyConfig proxyConfig;

    private final List<String> proxies;
    private final List<String> servers;

    private final Map<String, ServerInfo> lobbies;
    private final Map<String, ServerInfo> premiums;
    private final Map<String, ServerInfo> silent;

    private Channel friendChannel;
    private Channel playerChannel;
    private Channel proxyChannel;
    private Channel serverChannel;
    private Channel wrapperChannel;

    private FriendHandlerClient friendHandlerClient;
    private PlayerHandlerClient playerHandlerClient;
    private ProxyHandlerClient proxyHandlerClient;
    private ServerHandlerClient serverHandlerClient;
    private WrapperHandlerClient wrapperHandlerClient;

    public CloudProxiedPlugin() {
        instance = this;

        this.proxies = new ArrayList<>();
        this.servers = new ArrayList<>();

        this.lobbies = new HashMap<>();
        this.premiums = new HashMap<>();
        this.silent = new HashMap<>();
    }

    @Override
    public void onEnable() {
        this.cloudCore = new CloudCore("service.json");
        this.proxyConfig = new ProxyConfig(this);
        this.proxyConfig.readConfig();

        this.friendHandlerClient = new FriendHandlerClient(this, "127.0.0.1", 2299);
        this.playerHandlerClient = new PlayerHandlerClient(this, "127.0.0.1", 2300);
        this.proxyHandlerClient = new ProxyHandlerClient(this, "127.0.0.1", 2301);
        this.serverHandlerClient = new ServerHandlerClient(this, "127.0.0.1", 2302);
        this.wrapperHandlerClient = new WrapperHandlerClient(this, "127.0.0.1", 2303);

        new CloudCommand(this);

        new PlayerDisconnectListener(this);
        new PostLoginListener(this);
        new ProxyPingListener(this);
        new ServerConnectListener(this);
        new ServerSwitchListener(this);

        new Thread(this.playerHandlerClient).start();
        new Thread(this.proxyHandlerClient).start();
        new Thread(this.serverHandlerClient).start();
        new Thread(this.wrapperHandlerClient).start();

    }

    @Override
    public void onDisable() {

        ProxyServerStopPacket proxyServerStopPacket = new ProxyServerStopPacket(this.cloudCore.getName(), StopReason.Normal_STOP, this.cloudCore.getHostName(), this.cloudCore.getPort(),
                this.cloudCore.getServergroup(), this.cloudCore.getTemplate(), this.cloudCore.getWrapper(), this.cloudCore.getMinMemory(), this.cloudCore.getMaxMemory(), this.cloudCore.getIsDynamic());
        this.proxyChannel.writeAndFlush(new Packet(PacketType.ProxyServerStopPacket.name(), proxyServerStopPacket));

        if (this.wrapperChannel.isOpen() && this.wrapperChannel.isActive() && this.wrapperChannel.isRegistered()) {
            this.wrapperChannel.close();
        }

    }

    public Channel getPlayerChannel() {
        return playerChannel;
    }

    public void setPlayerChannel(Channel playerChannel) {
        this.playerChannel = playerChannel;
    }

    public Channel getProxyChannel() {
        return proxyChannel;
    }

    public void setProxyChannel(Channel proxyChannel) {
        this.proxyChannel = proxyChannel;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getWrapperChannel() {
        return wrapperChannel;
    }

    public void setWrapperChannel(Channel wrapperChannel) {
        this.wrapperChannel = wrapperChannel;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }


}

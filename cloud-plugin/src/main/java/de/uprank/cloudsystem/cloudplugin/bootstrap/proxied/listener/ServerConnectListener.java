package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class ServerConnectListener implements Listener {

    private final CloudProxiedPlugin plugin;

    public ServerConnectListener(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onServerConnectEvent(ServerConnectEvent event) {

        if (!(event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY))) return;

        List<ServerInfo> serverInfos = ProxyServer.getInstance().getServers().values().stream().toList();

        ServerInfo serverInfo = serverInfos.get(new Random().nextInt(serverInfos.size()));

        if (serverInfo.getName().contains("lobby")) {
            event.setTarget(serverInfo);
        }

    }

}

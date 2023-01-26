package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
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

        if (event.getPlayer().hasPermission("de.uprank.cloud.priority.level3")) {
            int random = new Random().nextInt(this.plugin.getSilent().size());
            List<ServerInfo> silents = this.plugin.getSilent().values().stream().toList();
            event.setTarget(silents.get(random));
        } else if (event.getPlayer().hasPermission("de.uprank.cloud.priority.level2")) {
            int random = new Random().nextInt(this.plugin.getPremiums().size());
            List<ServerInfo> premiums = this.plugin.getPremiums().values().stream().toList();
            event.setTarget(premiums.get(random));
        } else {
            int random = new Random().nextInt(this.plugin.getLobbies().size());
            List<ServerInfo> lobbies = this.plugin.getLobbies().values().stream().toList();
            event.setTarget(lobbies.get(random));
            event.getPlayer().connect(lobbies.get(random));
        }

    }

}

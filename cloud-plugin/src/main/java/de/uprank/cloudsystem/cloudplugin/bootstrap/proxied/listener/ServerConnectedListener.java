package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    private final CloudProxiedPlugin plugin;

    public ServerConnectedListener(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onServerConnectedEvent(ServerConnectedEvent event) {

        this.plugin.getCloudCore().getJedis().lpush("cloudPlayers", event.getPlayer().getUniqueId().toString());

        if (!(this.plugin.getCloudCore().getJedis().hexists("uuid", event.getPlayer().getName())))
            this.plugin.getCloudCore().getJedis().hset("uuid", event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
        if (!(this.plugin.getCloudCore().getJedis().hexists("name", event.getPlayer().getUniqueId().toString())))
            this.plugin.getCloudCore().getJedis().hset("name", event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());

        event.getPlayer().sendMessage(this.plugin.getProxyConfig().getPrefix() + "asdas");

    }

}

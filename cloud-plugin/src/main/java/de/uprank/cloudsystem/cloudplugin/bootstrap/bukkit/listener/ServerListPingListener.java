package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.listener;

import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    private final CloudBukkitPlugin plugin;

    public ServerListPingListener(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent event) {
        event.setMotd(this.plugin.getCloudCore().getAbstractGameServerManager().getGameServer(this.plugin.getCloudCore().getName()).getMotd());
    }

}

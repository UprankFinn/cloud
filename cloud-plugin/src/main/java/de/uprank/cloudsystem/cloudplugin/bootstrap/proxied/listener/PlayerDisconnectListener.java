package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLogOutPacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    private final CloudProxiedPlugin plugin;

    public PlayerDisconnectListener(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {

        this.plugin.getChannel().writeAndFlush(new Packet(PacketType.PlayerLogOutPacket.name(), new PlayerLogOutPacket(event.getPlayer().getUniqueId(), event.getPlayer().getName())));

        this.plugin.getCloudCore().getJedis().lrem("cloudPlayers", 1, event.getPlayer().getUniqueId().toString());

        if (this.plugin.getCloudCore().getJedis().hexists("uuid", event.getPlayer().getName())) {
            this.plugin.getCloudCore().getJedis().hdel("uuid", event.getPlayer().getName());
        }
        if (this.plugin.getCloudCore().getJedis().hexists("name", event.getPlayer().getUniqueId().toString())) {
            this.plugin.getCloudCore().getJedis().hdel("name", event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
        }

    }

}

package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerLoginPacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    private final CloudProxiedPlugin plugin;

    public PostLoginListener(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onPostLoginEvent(PostLoginEvent event) {
        this.plugin.getChannel().writeAndFlush(new Packet(PacketType.PlayerLoginPacket.name(), new PlayerLoginPacket(event.getPlayer().getUniqueId(), event.getPlayer().getName())));



    }

}

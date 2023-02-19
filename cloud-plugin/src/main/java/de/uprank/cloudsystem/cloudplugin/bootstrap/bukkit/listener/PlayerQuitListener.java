package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.listener;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerQuitListener implements Listener {

    private final CloudBukkitPlugin plugin;

    public PlayerQuitListener(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Map<String, Object> data = new HashMap<>();
        data.put("onlinePlayers", Bukkit.getOnlinePlayers().size() - 1);

        this.plugin.getServerChannel().writeAndFlush(new Packet(PacketType.GameServerUpdatePacket.name(), new GameServerUpdatePacket(this.plugin.getCloudCore().getName(), data)));

    }

}

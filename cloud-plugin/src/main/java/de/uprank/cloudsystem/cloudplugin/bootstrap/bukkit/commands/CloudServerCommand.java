package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.commands;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.database.signs.CloudSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloudServerCommand implements CommandExecutor {

    private final CloudBukkitPlugin plugin;

    public CloudServerCommand(CloudBukkitPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("cloudserver").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player = (Player) commandSender;

        if (args.length == 0) {

            player.sendMessage("§bCloudSystem §8» §rHere you can see all available Commands: ");
            player.sendMessage("§bCloudSystem §8»  §7cloudserver info");

        } else if (args.length == 1) {

            player.sendMessage("name: " + this.plugin.getCloudCore().getCurrentServiceName());
            player.sendMessage("gameId: " + this.plugin.getCloudCore().getGameId());
            player.sendMessage("onlinePlayers: §e" + Bukkit.getOnlinePlayers().size() + "§8/§6" + Bukkit.getMaxPlayers());

        }

        return false;
    }
}

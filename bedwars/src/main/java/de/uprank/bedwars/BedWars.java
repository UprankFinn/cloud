package de.uprank.bedwars;

import de.uprank.bedwars.team.Team;
import de.uprank.bedwars.team.TeamManager;
import de.uprank.cloudsystem.cloudapi.CloudAPI;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BedWars extends JavaPlugin {

    public static final String PREFIX = "§8[§cBedWars§8] §r";
    public static final String VARIANT = CloudAPI.getInstance().getCurrentTemplate();
    public static final GameServer GAMESERVER = CloudAPI.getInstance().getGameServerManager().getGameServer(CloudAPI.getInstance().getName());

    private final TeamManager teamManager;

    public BedWars() {
        this.teamManager = new TeamManager();
    }

    @Override
    public void onEnable() {

        this.teamManager.createTeam(new Team("Spectator", 100, null, ChatColor.GRAY, new HashMap<>()));

        Map<String, Object> settings = new HashMap<>();
        settings.put("bed", true);

        if (VARIANT.startsWith("2")) {
            this.teamManager.createTeam(new Team("Blau", 1, null, ChatColor.BLUE, settings));
            this.teamManager.createTeam(new Team("Rot", 2, null, ChatColor.RED, settings));
        } else if (VARIANT.startsWith("4")) {
            this.teamManager.createTeam(new Team("Blau", 1, null, ChatColor.BLUE, settings));
            this.teamManager.createTeam(new Team("Rot", 2, null, ChatColor.RED, settings));
            this.teamManager.createTeam(new Team("Grün", 3, null, ChatColor.GREEN, settings));
            this.teamManager.createTeam(new Team("Gelb", 4, null, ChatColor.YELLOW, settings));
        } else if (VARIANT.startsWith("8")) {
            this.teamManager.createTeam(new Team("Blau", 1, null, ChatColor.BLUE, settings));
            this.teamManager.createTeam(new Team("Rot", 2, null, ChatColor.RED, settings));
            this.teamManager.createTeam(new Team("Grün", 3, null, ChatColor.GREEN, settings));
            this.teamManager.createTeam(new Team("Gelb", 4, null, ChatColor.YELLOW, settings));

            this.teamManager.createTeam(new Team("Orange", 5, null, ChatColor.GOLD, settings));
            this.teamManager.createTeam(new Team("Schwarz", 6, null, ChatColor.BLACK, settings));
            this.teamManager.createTeam(new Team("Pink", 7, null, ChatColor.LIGHT_PURPLE, settings));
            this.teamManager.createTeam(new Team("Türkis", 8, null, ChatColor.AQUA, settings));
        }

    }

    @Override
    public void onDisable() {

    }
}

package de.uprank.bedwars.inventory;

import de.uprank.bedwars.BedWars;
import de.uprank.bedwars.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamInventory {

    private final BedWars plugin;

    private final Inventory inventory;

    public TeamInventory(BedWars plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 3 * 9, "Teamauswahl");

        List<Team> teams = new ArrayList<>();
        List<Team> sortedTeams = getItemsById(teams);

        sortedTeams.forEach((team) -> {
            if (team.getName().equals("Spectator")) return;
            this.inventory.addItem(team.getItemStack());
        });

    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Team> getItemsById(List<Team> teams) {
        teams.sort((Comparator.comparingInt(Team::getId)));
        return teams;
    }

}

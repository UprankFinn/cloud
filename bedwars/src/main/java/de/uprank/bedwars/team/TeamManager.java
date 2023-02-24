package de.uprank.bedwars.team;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private final Map<String, Team> teams;

    public TeamManager() {
        this.teams = new HashMap<>();
    }

    public void createTeam(Team team) {
        this.teams.put(team.getName(), team);
    }

    public Team getTeam(String team) {
        if (this.teams.containsKey(team)) return this.teams.get(team);
        return null;
    }

    public Team getTeamOfPlayer(Player player) {
        for (Team team : this.teams.values()) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return this.teams.get("Spectator");
    }

    public void removeTeam(String teamName) {
        this.teams.remove(teamName);
    }

    public Map<String, Team> getTeams() {
        return teams;
    }
}

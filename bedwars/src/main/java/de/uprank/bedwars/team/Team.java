package de.uprank.bedwars.team;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Team {

    private final String name;
    private final Integer id;

    private final ItemStack itemStack;

    private final ChatColor chatColor;
    private final List<Player> players;
    private final Map<String, Object> settings;

    public Team(String name, Integer id, ItemStack itemStack, ChatColor chatColor, Map<String, Object> settings) {
        this.name = name;
        this.id = id;

        this.itemStack = itemStack;

        this.chatColor = chatColor;
        this.players = new ArrayList<>();
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }
}

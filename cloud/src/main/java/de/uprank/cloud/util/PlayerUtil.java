package de.uprank.cloud.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtil {

    @Getter
    public static Map<UUID, String> players = new HashMap<>();

    public static void addPlayer(UUID uniqueId, String name) {
        players.put(uniqueId, name);
    }

    public static void removePlayer(UUID uniqueId) {
        players.remove(uniqueId);
    }

}

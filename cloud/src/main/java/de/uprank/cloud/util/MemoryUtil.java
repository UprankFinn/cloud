package de.uprank.cloud.util;

public class MemoryUtil {

    public static Integer getMemoryForService(String service) {
        Integer maxTeams = Integer.valueOf(service.split("x")[0]);
        Integer maxPlayers = Integer.valueOf(service.split("x")[1]);

        if (maxTeams != null && maxPlayers != null) {
            return 256;
        } else if (service.contains("lobby") || service.contains("Lobby")) {
            return 512;
        }

        return 256;
    }

}

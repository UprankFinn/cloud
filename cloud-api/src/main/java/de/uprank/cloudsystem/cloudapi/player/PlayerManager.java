package de.uprank.cloudsystem.cloudapi.player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PlayerManager {

    List<Player> getCloudPlayer();

    Player getCloudPlayer(UUID uniqueId);
    Player getCloudPlayer(String name);

}

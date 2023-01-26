package de.uprank.cloud.module.master.servers;

import de.uprank.cloud.module.master.MasterModule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ServerManager {

    private final MasterModule masterModule;

    private final Map<String, Server> servers = new HashMap<>();

    public ServerManager(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    public Server getServer(String name) {
        for (Server server : this.servers.values()) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

}

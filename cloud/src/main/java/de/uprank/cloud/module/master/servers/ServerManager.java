package de.uprank.cloud.module.master.servers;

import de.uprank.cloud.module.master.MasterModule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ServerManager {

    private final MasterModule masterModule;

    private final List<String> servers = new ArrayList<>();

    public ServerManager(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    public String getServer(String name) {
        for (String server : this.servers) {
            if (server.equals(name)) {
                return server;
            }
        }
        return null;
    }

}

package de.uprank.cloud.module.master.command.type;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerGroupsCommand implements Command {

    private final MasterModule masterModule;

    public ServerGroupsCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {

        List<File> files = Arrays.asList(new File("groups/").listFiles());
        List<String> groups = new ArrayList<>();
        files.forEach((folders) -> groups.add(folders.getName().replace(".json", "")));

        if (groups.isEmpty()) {
            this.masterModule.info("&cCurrently there are no serverGroups registered!");
            return;
        }

        this.masterModule.info("&bThere are all registered serverGroups in the cloudmaster:");

        groups.forEach((servergroups) -> this.masterModule.info("&b" + servergroups + " &b- &b" + this.masterModule.getServergroups().get(servergroups).iterator().next()));

    }
}

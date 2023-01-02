package de.uprank.cloud.module.master.command.type;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;

public class ReloadCommand implements Command {

    private final MasterModule masterModule;

    public ReloadCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {

        this.masterModule.info("&breload all data...");

    }
}

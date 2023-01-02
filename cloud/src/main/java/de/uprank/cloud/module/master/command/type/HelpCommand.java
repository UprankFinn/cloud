package de.uprank.cloud.module.master.command.type;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;

public class HelpCommand implements Command {

    private final MasterModule masterModule;

    public HelpCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {
        this.masterModule.info("Available Commands for the &bCloudMaster&r:");
        this.masterModule.info("  &8» &bclear   &8- &rclear the console screen");
        this.masterModule.info("  &8» &bcreate  &8- &rcreate a servergroup/template");
        this.masterModule.info("  &8» &breload  &8- &rreload all data");
        this.masterModule.info("  &8» &bservice &8- &rhandle active services");
        this.masterModule.info("  &8» &bstop    &8- &rstop the process");
    }
}

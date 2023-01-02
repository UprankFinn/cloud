package de.uprank.cloud.module.master.command.type;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;

public class WrappersCommand implements Command {

    private final MasterModule masterModule;

    public WrappersCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {

        if (this.masterModule.getWrapperManager().getWrapperList().isEmpty()) {
            this.masterModule.info("&cCurrently there are no wrappers registered!");
            return;
        }

        this.masterModule.getWrapperManager().getWrapperList().forEach((wrapper) -> {
            this.masterModule.info("Wrapper " + wrapper.getName() + " + " + wrapper.getHostName() + " + " + wrapper.getTimeout());
        });
    }
}

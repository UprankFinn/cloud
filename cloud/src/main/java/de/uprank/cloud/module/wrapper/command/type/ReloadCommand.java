package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.Command;

public class ReloadCommand implements Command {

    private final WrapperModule wrapperModule;

    public ReloadCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {

        this.wrapperModule.info("&breload all data...");

    }
}

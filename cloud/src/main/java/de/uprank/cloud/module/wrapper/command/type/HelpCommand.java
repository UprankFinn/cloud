package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.Command;

public class HelpCommand implements Command {

    private final WrapperModule wrapperModule;

    public HelpCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {
        this.wrapperModule.info("Available Commands for the &bCloudWrapper&r:");
        this.wrapperModule.info("  &8» &bclear   &8- &rclear the console screen");
        this.wrapperModule.info("  &8» &breload  &8- &rreload all data");
        this.wrapperModule.info("  &8» &bserver &8- &rhandle active servers on this wrapper");
        this.wrapperModule.info("  &8» &bproxy &8- &rhanddle active proxies on this wrapper");
        this.wrapperModule.info("  &8» &bstop    &8- &rstop the process");
    }
}

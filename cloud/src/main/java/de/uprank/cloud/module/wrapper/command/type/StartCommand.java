package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.Command;

import java.util.UUID;

public class StartCommand implements Command {

    private final WrapperModule wrapperModule;

    public StartCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {

        if (args.length == 1) {
            String group = args[0];
            String template = args[1];
            String gameId = UUID.randomUUID().toString().split("-")[0];
            Integer minMemory = 512;
            Integer maxMemory = 512;
            Boolean isFallback = true;
            Boolean isDynamic = true;

            this.wrapperModule.getServerManager().startService(group, template, gameId, minMemory, maxMemory, isFallback, isDynamic, 1);

        }

    }
}

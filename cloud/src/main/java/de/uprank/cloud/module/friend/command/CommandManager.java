package de.uprank.cloud.module.friend.command;

import de.uprank.cloud.module.wrapper.WrapperModule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final WrapperModule wrapperModule;

    private final Map<String, Command> commands;

    public CommandManager(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
        this.commands = new HashMap<>();
    }

    public void executeCommand(String input) {
        String command = input.split(" ")[0];
        String[] args = input.split(" ").length == 1 ? new String[0] : (String[]) Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);
        Command commandClass = this.commands.get(command);
        if (commandClass == null) commandClass = this.commands.get("help");
        commandClass.execute(command, args);
    }

    public void registerCommand(String name, Command command) {
        this.commands.put(name.toLowerCase(), command);
    }

    public void unregisterCommand(String name) {
        this.commands.remove(name.toLowerCase());
    }

    public Map<String, Command> getCommands() {
        return this.commands;
    }

}

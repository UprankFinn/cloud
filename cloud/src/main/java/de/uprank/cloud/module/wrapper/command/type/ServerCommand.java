package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.Command;

public class ServerCommand implements Command {

    private final WrapperModule wrapperModule;

    public ServerCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {

        if (args.length == 1) {
            if (args[0].equals("list")) {

                if (this.wrapperModule.getServerManager().getServers().isEmpty()) {
                    this.wrapperModule.info("&cCurrently there are no servers registered!");
                    return;
                }

                this.wrapperModule.info("&bThere are all registered servers in this wrapper:");
                this.wrapperModule.getServerManager().getServers().forEach((name, servers) -> {

                    this.wrapperModule.info("  &8» &b" + servers.getName() + " &8- &bregistered as " + servers.getHostName() + ":" + servers.getPort() + " &8(&aGameID #" + servers.getGameId() + "&8)");

                });

            }
        } else if (args.length == 2) {

            String serverName = args[0];

            if (args[1].equals("stop")) {


                if (this.wrapperModule.getServerManager().getServers().isEmpty()) {
                    this.wrapperModule.info("&c" + serverName + " is not started!");
                    return;
                }

                if(this.wrapperModule.getServerManager().getServer(serverName) == null){
                    this.wrapperModule.info("&c" + serverName + " is not started!");
                    return;
                }

                this.wrapperModule.getServerManager().getServer(serverName).shutdown();

            }

        }
    }
}

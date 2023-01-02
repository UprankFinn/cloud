package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.commands;

import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CloudCommand extends Command {

    private final CloudProxiedPlugin plugin;

    public CloudCommand(CloudProxiedPlugin plugin) {
        super("cloud", "de.uprank.cloudproxy.command.cloud");
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer proxiedPlayer)) return;

        if (args.length == 0) {

            proxiedPlayer.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + "§7Here you can see all available cloud commands:"));
            proxiedPlayer.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + " §8» §bcloud list servers/proxies"));

        }

        if (args.length == 2) {

            if (args[0].equals("list")) {

                if (args[1].equals("servers")) {

                    this.plugin.getProxy().getServers().forEach((s, server) -> proxiedPlayer.sendMessage(new TextComponent(this.plugin.getProxyConfig().getPrefix() + "§b" + server.getName() + " §8(§b" + server.getPlayers().size() + "§8)")));

                } else if (args[1].equals("proxies")) {


                }

            }

        }

    }
}

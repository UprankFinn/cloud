package de.uprank.cloudsystem.cloudapi.events;

import net.md_5.bungee.api.plugin.Event;

public class GameServerStopEvent extends Event {

    private final String server;

    public GameServerStopEvent(String server) {
        this.server = server;
    }
}

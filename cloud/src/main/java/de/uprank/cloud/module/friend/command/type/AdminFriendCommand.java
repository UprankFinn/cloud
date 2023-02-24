package de.uprank.cloud.module.friend.command.type;

import de.uprank.cloud.module.friend.FriendModule;
import de.uprank.cloud.module.friend.command.Command;

public class AdminFriendCommand implements Command {

    private final FriendModule friendModule;

    public AdminFriendCommand(FriendModule friendModule) {
        this.friendModule = friendModule;
    }

    @Override
    public void execute(String command, String[] args) {

    }
}

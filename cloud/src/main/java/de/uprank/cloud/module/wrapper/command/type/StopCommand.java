package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.command.Command;
import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.wrapper.WrapperStopPacket;

public class StopCommand implements Command {

    private final WrapperModule wrapperModule;

    public StopCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {
        this.wrapperModule.info("Stop the Java process");
        if (this.wrapperModule.getWrapperChannel() != null && this.wrapperModule.getWrapperChannel().isActive() && this.wrapperModule.getWrapperChannel().isOpen()) {
            this.wrapperModule.getWrapperChannel().writeAndFlush(new Packet(PacketType.WrapperStopPacket.name(), new WrapperStopPacket(this.wrapperModule.getName(), this.wrapperModule.getHostName())));

        }
        System.exit(0);
    }
}

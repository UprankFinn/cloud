package de.uprank.cloud.module.wrapper.command.type;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.Command;
import org.jline.utils.InfoCmp;

public class ClearCommand implements Command {

    private final WrapperModule wrapperModule;

    public ClearCommand(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void execute(String command, String[] args) {
        this.wrapperModule.getLineReader().getTerminal().puts(InfoCmp.Capability.clear_screen);
        this.wrapperModule.getLineReader().getTerminal().flush();
    }

}

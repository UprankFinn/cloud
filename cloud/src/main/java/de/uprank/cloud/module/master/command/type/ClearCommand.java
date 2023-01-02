package de.uprank.cloud.module.master.command.type;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;
import org.jline.utils.InfoCmp;

public class ClearCommand implements Command {

    private final MasterModule masterModule;

    public ClearCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {
        this.masterModule.getLineReader().getTerminal().puts(InfoCmp.Capability.clear_screen);
        this.masterModule.getLineReader().getTerminal().flush();
    }
}

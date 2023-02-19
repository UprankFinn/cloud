package de.uprank.cloud.module.master;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.uprank.cloud.module.Module;
import de.uprank.cloud.module.master.command.type.*;
import de.uprank.cloud.module.master.database.signs.CloudSignManager;
import de.uprank.cloud.module.master.command.CommandManager;
import de.uprank.cloud.module.master.netty.player.PlayerHandlerServer;
import de.uprank.cloud.module.master.netty.proxy.ProxyHandlerServer;
import de.uprank.cloud.module.master.netty.server.ServerHandlerServer;
import de.uprank.cloud.module.master.netty.wrapper.WrapperHandlerServer;
import de.uprank.cloud.module.master.proxies.ProxyManager;
import de.uprank.cloud.module.master.servers.ServerManager;
import de.uprank.cloud.module.master.servers.sign.SignManager;
import de.uprank.cloud.module.master.wrapper.WrapperManager;
import de.uprank.cloud.util.AnsiUtil;
import io.netty.channel.Channel;
import io.netty.util.ResourceLeakDetector;
import lombok.Getter;
import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
public class MasterModule extends Module {

    @Getter
    private static MasterModule instance;

    private final Map<String, List<String>> servergroups = new HashMap<>();
    private final List<String> groups = new ArrayList<>();

    private final SimpleFormatter simpleFormatter;
    private final SimpleDateFormat simpleDateFormat;
    private final Logger logger;
    private final LineReader lineReader;

    private boolean running;
    private boolean waiting;

    private final CommandManager commandManager;
    private final WrapperManager wrapperManager;
    private final ProxyManager proxyManager;
    private final ServerManager serverManager;

    private final SignManager signManager;

    private final Map<UUID, String> userOnProxy;
    private final Map<UUID, String> userOnServer;

    private Channel playerChannel;
    private Channel proxyChannel;
    private Channel serverChannel;
    private Channel wrapperChannel;

    private PlayerHandlerServer playerHandlerServer;
    private ProxyHandlerServer proxyHandlerServer;
    private ServerHandlerServer serverHandlerServer;
    private WrapperHandlerServer wrapperHandlerServer;

    private MongoClient mongoClient;

    private CloudSignManager cloudSignManager;

    public MasterModule() throws IOException {
        super("master");

        instance = this;

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        AnsiConsole.systemInstall();
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        this.simpleFormatter = new SimpleFormatter();
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.commandManager = new CommandManager(this);

        TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        terminalBuilder.encoding(Charset.defaultCharset());
        terminalBuilder.system(true);
        LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
        lineReaderBuilder.terminal(terminalBuilder.build());
        lineReaderBuilder.completer(new Completers.TreeCompleter(new Completers.TreeCompleter.Node[0]));
        lineReaderBuilder.parser(new DefaultParser());
        this.lineReader = lineReaderBuilder.build();

        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://root:mVEXrxgsqyKFhsdfiEuApmmkht3MjPuz@45.142.115.211:27017/admin"));
        this.cloudSignManager = new CloudSignManager(this);

        File file = new File("history");
        file.mkdirs();
        String var10002 = file.getCanonicalPath();
        FileHandler fileHandler = new FileHandler(var10002 + "/" + this.simpleDateFormat.format(new Date()) + "-%g.log", 5242880, 100, false);
        fileHandler.setFormatter(new SimpleFormatter());
        this.logger = Logger.getLogger("cloud");
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(fileHandler);

        this.wrapperManager = new WrapperManager(this);
        this.proxyManager = new ProxyManager(this);
        this.serverManager = new ServerManager(this);

        this.signManager = new SignManager(this);

        this.userOnProxy = new HashMap<>();
        this.userOnServer = new HashMap<>();
    }


    @Override
    public void onEnable() {
        this.running = true;
        this.waiting = false;

        this.commandManager.registerCommand("clear", new ClearCommand(this));
        this.commandManager.registerCommand("create", new CreateCommand(this));
        this.commandManager.registerCommand("help", new HelpCommand(this));
        this.commandManager.registerCommand("reload", new ReloadCommand(this));
        this.commandManager.registerCommand("servergroups", new ServerGroupsCommand(this));
        this.commandManager.registerCommand("stop", new StopCommand(this));
        this.commandManager.registerCommand("wrappers", new WrappersCommand(this));

        if (!(new File("groups/proxies/").exists())) new File("groups/proxies/").mkdirs();
        if (!(new File("groups/servers/").exists())) new File("groups/servers/").mkdirs();

        this.info("&b   _____ _                 _ __  __           _            ");
        this.info("&b  / ____| |               | |  \\/  |         | |           ");
        this.info("&b | |    | | ___  _   _  __| | \\  / | __ _ ___| |_ ___ _ __ ");
        this.info("&b | |    | |/ _ \\| | | |/ _` | |\\/| |/ _` / __| __/ _ \\ '__|");
        this.info("&b | |____| | (_) | |_| | (_| | |  | | (_| \\__ \\ ||  __/ |   ");
        this.info("&b  \\_____|_|\\___/ \\__,_|\\__,_|_|  |_|\\__,_|___/\\__\\___|_|   ");
        this.info("&b                                                           ");

        this.info("&rDiese Software wurde von &bFinn K. &rfür &cunkown.net &rentwickelt");
        this.info("&rAktuell verwendete Java Version &b" + System.getProperty("java.version"));
        this.info("&b");

        new Thread(() -> {
            while (this.running) {
                this.waiting = true;
                String line = null;

                try {
                    line = this.lineReader.readLine(AnsiUtil.format("&bCloudSystem &8> &r"), (String) null, (MaskingCallback) null, (String) null);
                } catch (UserInterruptException var4) {
                    System.exit(0);
                }

                this.waiting = false;
                if (line != null && !line.isEmpty()) {
                    this.commandManager.executeCommand(line.trim());
                }
            }
        }).start();

        this.playerHandlerServer = new PlayerHandlerServer(this, 2300);
        this.proxyHandlerServer = new ProxyHandlerServer(this, 2301);
        this.serverHandlerServer = new ServerHandlerServer(this, 2302);
        this.wrapperHandlerServer = new WrapperHandlerServer(this, 2303);

        new Thread(playerHandlerServer).start();
        new Thread(proxyHandlerServer).start();
        new Thread(serverHandlerServer).start();
        new Thread(wrapperHandlerServer).start();

        this.signManager.loadSigns();

        Runtime.getRuntime().addShutdownHook(new Thread(this::onDisable));

        this.servergroups.forEach((s, strings) -> this.groups.add(s));

    }

    @Override
    public void onDisable() {
        this.running = false;
        this.waiting = false;

        this.commandManager.unregisterCommand("help");
        this.commandManager.unregisterCommand("stop");
        //this.getMongoClient().close();
    }


    public void info(String message) {
        message = AnsiUtil.format(message);
        if (this.lineReader == null) {
            System.out.println(message);
        } else {
            if (this.waiting) {
                this.lineReader.callWidget("clear");
            }

            this.lineReader.getTerminal().writer().print(this.simpleFormatter.format(new LogRecord(java.util.logging.Level.INFO, message)));
            if (this.waiting) {
                this.lineReader.callWidget("redraw-line");
            }

            if (this.waiting) {
                this.lineReader.callWidget("redisplay");
            }

            this.lineReader.getTerminal().writer().flush();
            if (this.logger != null) {
                this.logger.info(message);
            }

        }
    }

    public void warning(String message) {
        message = AnsiUtil.format(message);
        if (this.lineReader == null) {
            System.out.println(message);
        } else {
            if (this.waiting) {
                this.lineReader.callWidget("clear");
            }

            this.lineReader.getTerminal().writer().print(this.simpleFormatter.format(new LogRecord(java.util.logging.Level.WARNING, message)));
            if (this.waiting) {
                this.lineReader.callWidget("redraw-line");
            }

            if (this.waiting) {
                this.lineReader.callWidget("redisplay");
            }

            this.lineReader.getTerminal().writer().flush();
            if (this.logger != null) {
                this.logger.warning(message);
            }

        }
    }

    public void severe(String message) {
        message = AnsiUtil.format(message);
        if (this.lineReader == null) {
            System.out.println(message);
        } else {
            if (this.waiting) {
                this.lineReader.callWidget("clear");
            }

            this.lineReader.getTerminal().writer().print(this.simpleFormatter.format(new LogRecord(java.util.logging.Level.SEVERE, message)));
            if (this.waiting) {
                this.lineReader.callWidget("redraw-line");
            }

            if (this.waiting) {
                this.lineReader.callWidget("redisplay");
            }

            this.lineReader.getTerminal().writer().flush();
            if (this.logger != null) {
                this.logger.severe(message);
            }

        }
    }

    public LineReader getLineReader() {
        return this.lineReader;
    }

    public SimpleFormatter getSimpleFormatter() {
        return this.simpleFormatter;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return this.simpleDateFormat;
    }

    public Channel getPlayerChannel() {
        return playerChannel;
    }

    public void setPlayerChannel(Channel playerChannel) {
        this.playerChannel = playerChannel;
    }

    public Channel getProxyChannel() {
        return proxyChannel;
    }

    public void setProxyChannel(Channel proxyChannel) {
        this.proxyChannel = proxyChannel;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getWrapperChannel() {
        return wrapperChannel;
    }

    public void setWrapperChannel(Channel wrapperChannel) {
        this.wrapperChannel = wrapperChannel;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public WrapperManager getWrapperManager() {
        return wrapperManager;
    }
}

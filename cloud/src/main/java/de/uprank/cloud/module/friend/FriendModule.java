package de.uprank.cloud.module.friend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.uprank.cloud.module.Module;
import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.command.CommandManager;
import de.uprank.cloud.util.AnsiUtil;
import io.netty.channel.Channel;
import io.netty.util.ResourceLeakDetector;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
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
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
@Setter
public class FriendModule extends Module {

    @Getter
    private static FriendModule instance;

    private final SimpleFormatter simpleFormatter;
    private final SimpleDateFormat simpleDateFormat;
    private final Logger logger;
    private final LineReader lineReader;

    private boolean running;
    private boolean waiting;

    private MongoClient mongoClient;

    private Channel channel;

    //private final CommandManager commandManager;

    public FriendModule() throws IOException {
        super("friend");

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        AnsiConsole.systemInstall();
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        this.simpleFormatter = new SimpleFormatter();
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //this.commandManager = new CommandManager(this);

        TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        terminalBuilder.encoding(Charset.defaultCharset());
        terminalBuilder.system(true);
        LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
        lineReaderBuilder.terminal(terminalBuilder.build());
        lineReaderBuilder.completer(new Completers.TreeCompleter(new Completers.TreeCompleter.Node[0]));
        lineReaderBuilder.parser(new DefaultParser());
        this.lineReader = lineReaderBuilder.build();

        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://root:mVEXrxgsqyKFhsdfiEuApmmkht3MjPuz@45.142.115.211:27017/admin"));

        File file = new File("history");
        file.mkdirs();
        String var10002 = file.getCanonicalPath();
        FileHandler fileHandler = new FileHandler(var10002 + "/" + this.simpleDateFormat.format(new Date()) + "-%g.log", 5242880, 100, false);
        fileHandler.setFormatter(new SimpleFormatter());
        this.logger = Logger.getLogger("cloud");
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(fileHandler);

    }

    @Override
    public void onEnable() {
        instance = this;
        this.running = true;
        this.waiting = false;

        Runtime.getRuntime().addShutdownHook(new Thread(this::onDisable));

        this.info("&b   _____ _                 ___          __                              ");
        this.info("&b  / ____| |               | \\ \\        / /                              ");
        this.info("&b | |    | | ___  _   _  __| |\\ \\  /\\  / / __ __ _ _ __  _ __   ___ _ __ ");
        this.info("&b | |    | |/ _ \\| | | |/ _` | \\ \\/  \\/ / '__/ _` | '_ \\| '_ \\ / _ \\ '__|");
        this.info("&b | |____| | (_) | |_| | (_| |  \\  /\\  /| | | (_| | |_) | |_) |  __/ |   ");
        this.info("&b  \\_____|_|\\___/ \\__,_|\\__,_|   \\/  \\/ |_|  \\__,_| .__/| .__/ \\___|_|   ");
        this.info("&b                                                 | |   | |              ");
        this.info("&b                                                 |_|   |_|              ");
        this.info("&b");

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
                    //this.commandManager.executeCommand(line.trim());
                }
            }
        }).start();

    }

    @Override
    public void onDisable() {
        this.running = false;
        this.waiting = false;
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

}

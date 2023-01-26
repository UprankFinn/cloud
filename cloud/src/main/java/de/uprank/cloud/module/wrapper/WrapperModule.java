package de.uprank.cloud.module.wrapper;

import com.sun.management.OperatingSystemMXBean;
import de.uprank.cloud.module.Module;
import de.uprank.cloud.module.wrapper.command.type.*;
import de.uprank.cloud.module.wrapper.command.CommandManager;
import de.uprank.cloud.module.wrapper.group.proxy.ProxyGroupManager;
import de.uprank.cloud.module.wrapper.group.server.ServiceGroupManager;
import de.uprank.cloud.module.wrapper.netty.NettyServer;
import de.uprank.cloud.module.wrapper.service.proxy.ProxyManager;
import de.uprank.cloud.module.wrapper.service.server.ServerManager;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.wrapper.WrapperAlivePacket;
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
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
public class WrapperModule extends Module {

    @Getter
    private static WrapperModule instance;

    private final SimpleFormatter simpleFormatter;
    private final SimpleDateFormat simpleDateFormat;
    private final Logger logger;
    private final LineReader lineReader;

    private boolean running;
    private boolean waiting;

    private final CommandManager commandManager;

    private Channel channel;
    private NettyServer nettyServer;

    private final ProxyGroupManager proxyGroupManager;
    private final ServiceGroupManager serviceGroupManager;
    private final ProxyManager proxyManager;
    private final ServerManager serverManager;

    private String name = "Wrapper-1";
    private String hostName = "127.0.0.1";
    private Integer port = 1234;
    private Integer maxMemory = 5000;

    private static ConcurrentHashMap<Long, Double> cpuAverage = new ConcurrentHashMap<>();

    public WrapperModule() throws IOException {
        super("wrapper");

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

        File file = new File("history");
        file.mkdirs();
        String var10002 = file.getCanonicalPath();
        FileHandler fileHandler = new FileHandler(var10002 + "/" + this.simpleDateFormat.format(new Date()) + "-%g.log", 5242880, 100, false);
        fileHandler.setFormatter(new SimpleFormatter());
        this.logger = Logger.getLogger("cloud");
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(fileHandler);

        this.proxyGroupManager = new ProxyGroupManager();
        this.serviceGroupManager = new ServiceGroupManager();
        this.proxyManager = new ProxyManager(this);
        this.serverManager = new ServerManager(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.running = true;
        this.waiting = false;

        this.commandManager.registerCommand("clear", new ClearCommand(this));
        this.commandManager.registerCommand("help", new HelpCommand(this));
        this.commandManager.registerCommand("reload", new ReloadCommand(this));
        this.commandManager.registerCommand("server", new ServerCommand(this));
        this.commandManager.registerCommand("stop", new StopCommand(this));

        if (!(new File("default/bukkit/plugins/").exists())) new File("default/bukkit/plugins/").mkdirs();
        if (!(new File("default/proxied/plugins/").exists())) new File("default/proxied/plugins/").mkdirs();


        if (!(new File("temporary/").exists())) new File("temporary/").mkdirs();
        if (!(new File("temporary/proxies/").exists())) new File("temporary/proxies/").mkdirs();
        if (!(new File("temporary/servers/").exists())) new File("temporary/servers/").mkdirs();

        if (!(new File("templates/").exists())) new File("templates/").mkdirs();
        if (!(new File("templates/proxies/").exists())) new File("templates/proxies/").mkdirs();
        if (!(new File("templates/servers/").exists())) new File("templates/servers/").mkdirs();

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
                    this.commandManager.executeCommand(line.trim());
                }
            }
        }).start();

        new Thread(() -> new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Thread(() -> {
                    if (channel != null && channel.isOpen() && channel.isActive()) {
                        double cpuUsage = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad() * 100;
                        cpuAverage.put(System.currentTimeMillis(), cpuUsage);
                        Integer usedMemory = 0;
                        Integer freeMemory = 0;
                        Integer mb = 1024 * 1024;
                        double totalMem = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / mb;
                        int totalMemory = (int) totalMem;
                        try {
                            /*Process getUsedProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", "awk '/^-/ {print $3}' <(free -m)"});
                            getUsedProcess.waitFor();
                            usedMemory = 12000;
                            Process getFreeProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", "awk '/^-/ {print $4}' <(free -m)"});
                            getFreeProcess.waitFor();
                            freeMemory = 12000;*/
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        List<Double> cpuAverageCalculate = new ArrayList<>();
                        for (long time : cpuAverage.keySet()) {
                            long diff = System.currentTimeMillis() - time;
                            if (diff > 60000) {
                                cpuAverage.remove(time);
                                continue;
                            }
                            cpuAverageCalculate.add(cpuAverage.get(time));
                        }
                        double cpuAverageUsage = calculateAverage(cpuAverageCalculate);
                        channel.writeAndFlush(new Packet(PacketType.WrapperAlivePacket.name(), new WrapperAlivePacket(name, hostName, usedMemory, freeMemory, maxMemory, cpuUsage, cpuAverageUsage)));
                    }
                }).start();
            }
        }, 20L, 20L)).start();

        if (this.name != null && hostName != null && maxMemory != -1) {
            this.nettyServer = new NettyServer(this);
            new Thread(this.nettyServer).start();
        }

    }

    @Override
    public void onDisable() {
        this.running = false;
        this.waiting = false;

        if (!(this.proxyManager.getProcesses().isEmpty())) {
            this.proxyManager.getProcesses().forEach((s, process) -> {
                try {
                    process.destroyForcibly().waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.proxyManager.getProcesses().remove(s);
            });
        }

        if (!(this.proxyManager.getProxies().isEmpty())) {
            this.proxyManager.getProxies().forEach((name, proxies) -> proxies.shutdown());
        }

        if (!(this.serverManager.getProcesses().isEmpty())) {
            this.serverManager.getProcesses().forEach((s, process) -> {
                try {
                    process.destroyForcibly().waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.serverManager.getProcesses().remove(s);
            });
        }

        if (!(this.serverManager.getServers().isEmpty())) {
            this.serverManager.getServers().forEach((name, servers) -> servers.shutdown());
        }

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

    public ServiceGroupManager getServiceGroupManager() {
        return serviceGroupManager;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
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

    private double calculateAverage(List<Double> marks) {
        Double sum = 0D;
        if (!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public NettyServer getNettyServer() {
        return nettyServer;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public static ConcurrentHashMap<Long, Double> getCpuAverage() {
        return cpuAverage;
    }
}

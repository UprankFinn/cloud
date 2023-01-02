package de.uprank.cloud;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.uprank.cloud.module.ModuleManager;
import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.wrapper.WrapperModule;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudSystem {

    private static MongoClient mongoClient;

    private static MasterModule masterModule;
    private static WrapperModule wrapperModule;

    @SneakyThrows
    public static void main(String[] args) {

        Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        Logger.getLogger("com.mongodb.diagnostics.logging.JULLogger").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.insert").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.query").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.update").setLevel(Level.OFF);

        masterModule = new MasterModule();
        wrapperModule = new WrapperModule();

        ModuleManager moduleManager = new ModuleManager();
        OptionParser optionParser = new OptionParser();

        optionParser.accepts("master", null);
        optionParser.accepts("wrapper", null);

        moduleManager.addModule("master", masterModule);
        moduleManager.addModule("wrapper", wrapperModule);

        OptionSet optionSet = optionParser.parse(args);

        if (optionSet == null || !optionSet.hasOptions() || optionSet.has("help")) {
            try {
                optionParser.printHelpOn(System.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        mongoClient = new MongoClient(new MongoClientURI("mongodb://root:mVEXrxgsqyKFhsdfiEuApmmkht3MjPuz@45.142.115.211:27017/admin"));

        if (optionSet.has("master")) {
            masterModule = (MasterModule) moduleManager.getModules().get("master");
            masterModule.onEnable();
        } else if (optionSet.has("wrapper")) {
            wrapperModule = (WrapperModule) moduleManager.getModules().get("wrapper");
            wrapperModule.onEnable();
        }

    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

}

package net.senmori.mobmerge;

import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.listener.EntityListener;
import net.senmori.mobmerge.tasks.ProcessWorldsTask;
import net.senmori.mobmerge.util.MobLogger;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MobMerge extends JavaPlugin {
    private static final boolean DEBUG = Boolean.valueOf(System.getProperty("mobMergeDebug", "false"));
    public static MobLogger LOG;
    public static MobMerge INSTANCE;

    private FileConfiguration config;
    private ConfigManager configManager;
    private ProcessWorldsTask processWorldsTask;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOG = new MobLogger(this);
        debug("Debug mode enabled! Was this on purpose? If not, then you really messed up. Ask for help in #spigot in irc.spi.gt");


        getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        config = getConfig();
        ConditionManager.init();
        configManager = new ConfigManager(this, config, new File(getDataFolder(), "config.yml"));

        processWorldsTask = new ProcessWorldsTask(configManager);
        new EntityListener(configManager);
    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
    }

    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(INSTANCE, key);
    }

    public static void debug(String message) {
        if(isDebugMode()) {
            LOG.info("[Debug] " + message);
        }
    }

    public static boolean isDebugMode() {
        return DEBUG;
    }
}

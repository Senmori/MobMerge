package net.senmori.mobmerge;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class MobMerge extends JavaPlugin {
    public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("mobMergeDebug", "false"));
    public static final Logger LOG = Logger.getLogger("MobMerge");

    private FileConfiguration config;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        config = getConfig();
        configManager = new ConfigManager(config);
    }

    @Override
    public void onDisable() {
    }
}

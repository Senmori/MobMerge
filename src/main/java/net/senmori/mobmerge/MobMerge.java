package net.senmori.mobmerge;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class MobMerge extends JavaPlugin {
    public static final boolean DEBUG = Boolean.getBoolean("mobMergeDebug");
    public static PluginLogger LOG;

    private FileConfiguration config;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        LOG = new PluginLogger(this);
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        config = getConfig();
        configManager = new ConfigManager(config, new File(getDataFolder(), "config.yml"));


    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
    }
}

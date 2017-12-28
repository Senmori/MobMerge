package net.senmori.mobmerge;

import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.listener.EntityListener;
import net.senmori.mobmerge.tasks.ProcessWorldsTask;
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
    private ProcessWorldsTask processWorldsTask;

    @Override
    public void onEnable() {
        LOG = new PluginLogger(this);
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        this.saveDefaultConfig();

        config = getConfig();
        configManager = new ConfigManager(this, config, new File(getDataFolder(), "config.yml"));

        processWorldsTask = new ProcessWorldsTask(configManager);
        new EntityListener(configManager);
    }

    @Override
    public void onDisable() {
        configManager.onDisable();
    }
}

package net.senmori.mobmerge;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.configuration.SettingsManager;
import net.senmori.mobmerge.listener.EntityListener;
import net.senmori.mobmerge.options.EntityOptionManager;
import net.senmori.mobmerge.tasks.ProcessWorldsTask;
import net.senmori.mobmerge.util.MobLogger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MobMerge extends JavaPlugin {
    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("mobMergeDebug", "false"));
    public static MobLogger LOG;
    private static MobMerge INSTANCE;

    public static MobMerge getInstance() {
        return INSTANCE;
    }

    private SettingsManager settingsManager;
    private EntityOptionManager entityOptionManager;
    private ProcessWorldsTask processWorldsTask;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOG = new MobLogger(this);
        debug("Debug mode enabled! Was this on purpose? If not, then you really messed up. Ask for help in #spigot in irc.spi.gt");

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        this.saveDefaultConfig();

        settingsManager = new SettingsManager(this, new File(getDataFolder(), "config.yml"));
        //settingsManager.load();

        processWorldsTask = new ProcessWorldsTask(settingsManager);
        new EntityListener(settingsManager);

    }

    @Override
    public void onDisable() {
        settingsManager.save(settingsManager.getConfig());
    }

    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(INSTANCE, key);
    }

    public static NamespacedKey parseStringToKey(String string) {
        if(StringUtil.isNullOrEmpty(string)) {
            return null;
        }
        if(!string.contains(":")) {
            return MobMerge.newKey(string);
        }
        String[] result = string.split(":");
        String pluginName = result[0];
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if(plugin != null) {
            return new NamespacedKey(plugin, result[1]);
        } else {
            return new NamespacedKey(result[0], result[1]);
        }
    }

    public static void debug(String message) {
        if(isDebugMode()) {
            LOG.info("[Debug] " + message);
        }
    }

    public static boolean isDebugMode() {
        return DEBUG;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}

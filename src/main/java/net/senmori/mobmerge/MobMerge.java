package net.senmori.mobmerge;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.condition.entity.ChargedCreeperCondition;
import net.senmori.mobmerge.condition.entity.EntityCustomNameCondition;
import net.senmori.mobmerge.condition.entity.EntityHasCustomNameCondition;
import net.senmori.mobmerge.condition.entity.EntityScoreboardTagCondition;
import net.senmori.mobmerge.condition.entity.MatchDyeColorCondition;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.listener.EntityListener;
import net.senmori.mobmerge.tasks.ProcessWorldsTask;
import net.senmori.mobmerge.util.MobLogger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MobMerge extends JavaPlugin {
    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("mobMergeDebug", "false"));
    public static MobLogger LOG;
    public static MobMerge INSTANCE;

    private ConfigManager configManager;
    private ProcessWorldsTask processWorldsTask;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOG = new MobLogger(this);
        debug("Debug mode enabled! Was this on purpose? If not, then you really messed up. Ask for help in #spigot in irc.spi.gt");

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        this.saveDefaultConfig();

        configManager = new ConfigManager(this, new File(getDataFolder(), "config.yml"));

        ConditionManager.getInstance().registerCondition(new ChargedCreeperCondition());
        ConditionManager.getInstance().registerCondition(new EntityCustomNameCondition());
        ConditionManager.getInstance().registerCondition(new EntityHasCustomNameCondition());
        ConditionManager.getInstance().registerCondition(new EntityScoreboardTagCondition());
        ConditionManager.getInstance().registerCondition(new MatchDyeColorCondition());

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
}

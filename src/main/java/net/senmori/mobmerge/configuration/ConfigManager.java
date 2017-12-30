package net.senmori.mobmerge.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.ConfigurationKey;
import net.senmori.mobmerge.configuration.option.types.BooleanOption;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
import net.senmori.mobmerge.configuration.option.types.StringListOption;
import net.senmori.mobmerge.configuration.option.types.VectorOption;
import net.senmori.mobmerge.configuration.option.types.WorldListOption;
import net.senmori.mobmerge.option.EntityOptionManager;
import net.senmori.mobmerge.tasks.UpdateConfigTask;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static final int TICKS_PER_SECOND = 20;
    public static EntityOptionManager entityOptionManager;
    private static final BiMap<ConfigurationKey, ConfigOption> options = HashBiMap.create();

    // class variables
    private final EntityOptionManager optionManager;
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final File configFile;
    private UpdateConfigTask task;

    // ConfigurationKeys
    public static final ConfigurationKey DEFAULT_RADIUS = ConfigurationKey.create("Default Radius", VectorOption.class);
    public static final ConfigurationKey DEFAULT_INTERVAL = ConfigurationKey.create("Default Interval", NumberOption.class);
    public static final ConfigurationKey DEFAULT_COUNT = ConfigurationKey.create("Default Count", NumberOption.class);
    public static final ConfigurationKey DEFAULT_CHAT_COLOR = ConfigurationKey.create("Default Chat Color", ChatColorOption.class);
    public static final ConfigurationKey EXCLUDED_WORLDS_KEY = ConfigurationKey.create("Excluded Worlds", StringListOption.class);
    public static final ConfigurationKey VERBOSE_KEY = ConfigurationKey.create("Verbose", BooleanOption.class);
    public static final ConfigurationKey UPDATE_CONFIG_KEY = ConfigurationKey.create("Update Config Interval", NumberOption.class);

    // Special config keys that are useful, but should not be registered to anything in particular
    public static final ConfigurationKey CONDITIONS_KEY = ConfigurationKey.create("conditions", null);
    public static final ConfigurationKey MOBS_KEY = ConfigurationKey.create("mobs", null);

    // Options
    public static final VectorOption RADIUS = registerOption(DEFAULT_RADIUS, VectorOption.newOption("default.radius", 5));
    public static final NumberOption INTERVAL = registerOption(DEFAULT_INTERVAL, NumberOption.newOption("default.interval", 5));
    public static final NumberOption MAX_COUNT = registerOption(DEFAULT_COUNT, NumberOption.newOption("default.count", 256));
    public static final NumberOption CONFIG_UPDATE_INTERVAL = registerOption(UPDATE_CONFIG_KEY, NumberOption.newOption("config.update-interval", 5));
    public static final ChatColorOption DEFAULT_COLOR = registerOption(DEFAULT_CHAT_COLOR, ChatColorOption.newOption("default.color", ChatColor.RED));
    public static final BooleanOption VERBOSE = registerOption(VERBOSE_KEY, BooleanOption.newOption("verbose", true));
    public static final WorldListOption EXCLUDED_WORLDS = registerOption(EXCLUDED_WORLDS_KEY, WorldListOption.newOption("excluded-worlds", Lists.newArrayList()));

    public ConfigManager(JavaPlugin plugin, FileConfiguration config, File configFile) {
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.configFile = configFile;
        this.optionManager = new EntityOptionManager(this);
        entityOptionManager = this.optionManager;
        loadConfig();
    }


    public static <T extends ConfigOption> T registerOption(ConfigurationKey key, T option) {
        options.put(key, option);
        return option;
    }

    public static <T extends ConfigOption> T getOption(ConfigurationKey key) {
        return (T) key.getType().cast(options.get(key));
    }

    public static <T extends ConfigOption> T getOptionByPath(String path) {
        return (T)options.values().stream().filter(c -> c.getPath().equals(path)).limit(1);
    }

    public static ConfigurationKey getConfigurationKey(ConfigOption option) {
        return options.inverse().getOrDefault(option, ConfigurationKey.NULL_KEY);
    }

    /*
     * NON-STATIC METHODS BELOW HERE
     */
    private void updateOptions() {
        VERBOSE.load(getConfig());
        options.values().forEach(opt -> {
            if(!opt.load(getConfig())) {
                if(VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Option " + opt.getPath() + " failed to load from config");
                }
            }
        });
    }

    public void loadConfig() {
        updateOptions();
        optionManager.load(getConfig());
        if(CONFIG_UPDATE_INTERVAL.getValue().intValue() < 0) {
            if(task != null) {
                int attempts = 0;
                do {
                    task.cancel();
                }while(!task.isCancelled());
                task.reset();
            } else {
                task = new UpdateConfigTask(this);
            }
        }
    }

    public void saveConfig() {
        options.values().forEach( opt -> {
            opt.save(getConfig());
        });

        save();
    }

    private void save() {
        try {
            getConfig().save(getConfigFile());
        } catch (IOException e) {
            MobMerge.LOG.severe("Error saving config file " + configFile.getAbsolutePath());
        }
    }

    public EntityOptionManager getEntityOptionManager() {
        return optionManager;
    }

    public static EntityOptionManager getEntityManager() {
        return entityOptionManager;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }
}

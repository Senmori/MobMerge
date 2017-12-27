package net.senmori.mobmerge.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.ConfigurationKey;
import net.senmori.mobmerge.configuration.option.types.BooleanOption;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.EntityOptions;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
import net.senmori.mobmerge.configuration.option.types.StringListOption;
import net.senmori.mobmerge.configuration.option.types.WorldListOption;
import net.senmori.mobmerge.tasks.UpdateConfigTask;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager<T extends ConfigOption> {
    public static final int TICKS_PER_SECOND = 20;
    private static final BiMap<ConfigurationKey, ConfigOption> options = HashBiMap.create();

    // class variables
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final File configFile;
    private UpdateConfigTask task;

    // ConfigurationKeys
    public static final ConfigurationKey DEFAULT_RADIUS = ConfigurationKey.create("Default Radius", NumberOption.class);
    public static final ConfigurationKey DEFAULT_INTERVAL = ConfigurationKey.create("Default Interval", NumberOption.class);
    public static final ConfigurationKey DEFAULT_COUNT = ConfigurationKey.create("Default Count", NumberOption.class);
    public static final ConfigurationKey DEFAULT_CHAT_COLOR = ConfigurationKey.create("Default Chat Color", ChatColorOption.class);
    public static final ConfigurationKey MOBS_SECTION_KEY = ConfigurationKey.create("Mobs Section Key", EntityOptions.class);
    public static final ConfigurationKey EXCLUDED_WORLDS_KEY = ConfigurationKey.create("Excluded Worlds", StringListOption.class);
    public static final ConfigurationKey VERBOSE_KEY = ConfigurationKey.create("Verbose", BooleanOption.class);
    public static final ConfigurationKey UPDATE_CONFIG_KEY = ConfigurationKey.create("Update Config Interval", NumberOption.class);

    // Options
    public static final NumberOption RADIUS = registerOption(DEFAULT_RADIUS, NumberOption.newOption("default.radius", 5));
    public static final NumberOption INTERVAL = registerOption(DEFAULT_INTERVAL, NumberOption.newOption("default.interval", 5));
    public static final NumberOption MAX_COUNT = registerOption(DEFAULT_COUNT, NumberOption.newOption("default.count", 256));
    public static final NumberOption CONFIG_UPDATE_INTERVAL = registerOption(UPDATE_CONFIG_KEY, NumberOption.newOption("config.update-interval", 5));
    public static final ChatColorOption DEFAULT_COLOR = registerOption(DEFAULT_CHAT_COLOR, ChatColorOption.newOption("default.color", ChatColor.RED));
    public static final BooleanOption VERBOSE = registerOption(VERBOSE_KEY, BooleanOption.newOption("verbose", true));
    public static final EntityOptions MOBS = registerOption(MOBS_SECTION_KEY, EntityOptions.newOption("mobs", Lists.newArrayList()));
    public static final WorldListOption EXCLUDED_WORLDS = registerOption(EXCLUDED_WORLDS_KEY, WorldListOption.newOption("excluded-worlds", Lists.newArrayList()));

    public ConfigManager(JavaPlugin plugin, FileConfiguration config, File configFile) {
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.configFile = configFile;
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
    private void loadOptions() {
        VERBOSE.load(getConfig());
        for(ConfigOption opt : options.values()) {
            if (!opt.load(getConfig())) {
                if (VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Option " + opt.getPath() + " failed to load from config");
                }
            }
        }
    }

    private void loadConfig() {
        loadOptions();
        if(CONFIG_UPDATE_INTERVAL.getValue().intValue() < 0) {
            if(task != null) {
                task.cancel();
            }
            task = new UpdateConfigTask(this);
        }
    }

    public void onDisable() {
        saveConfig();
    }

    public void updateConfig() {
        saveConfig();
    }

    private void saveConfig() {
        options.values().forEach( opt -> {
            opt.save(getConfig());
        });
        try {
            getConfig().save(getConfigFile());
        } catch (IOException e) {
            MobMerge.LOG.severe("Error saving config file " + configFile.getAbsolutePath());
        }
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

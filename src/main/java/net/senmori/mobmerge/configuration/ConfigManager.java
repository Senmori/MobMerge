package net.senmori.mobmerge.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.ConfigurationKey;
import net.senmori.mobmerge.configuration.option.types.BooleanOption;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.EntityTypeListOption;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
import net.senmori.mobmerge.configuration.option.types.StringListOption;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager<T extends ConfigOption> {
    private static final BiMap<ConfigurationKey, ConfigOption> options = HashBiMap.create();

    private final FileConfiguration config;
    private final File configFile;

    // ConfigurationKeys
    public static final ConfigurationKey DEFAULT_RADIUS = ConfigurationKey.create("Default Radius", NumberOption.class);
    public static final ConfigurationKey DEFAULT_INTERVAL = ConfigurationKey.create("Default Interval", NumberOption.class);
    public static final ConfigurationKey DEFAULT_COUNT = ConfigurationKey.create("Default Count", NumberOption.class);
    public static final ConfigurationKey DEFAULT_CHAT_COLOR = ConfigurationKey.create("Default Chat Color", ChatColorOption.class);
    public static final ConfigurationKey MOBS_SECTION_KEY = ConfigurationKey.create("Mobs Section Key", EntityTypeListOption.class);
    public static final ConfigurationKey EXCLUDED_WORLDS_KEY = ConfigurationKey.create("Excluded Worlds", StringListOption.class);
    public static final ConfigurationKey VERBOSE_KEY = ConfigurationKey.create("Verbose", BooleanOption.class);

    // Options
    public static final NumberOption RADIUS = registerOption(DEFAULT_RADIUS, NumberOption.newOption("default.radius", 5));
    public static final NumberOption INTERVAL = registerOption(DEFAULT_INTERVAL, NumberOption.newOption("default.interval", 20));
    public static final NumberOption MAX_COUNT = registerOption(DEFAULT_COUNT, NumberOption.newOption("default.count", 256));
    public static final ChatColorOption DEFAULT_COLOR = registerOption(DEFAULT_CHAT_COLOR, ChatColorOption.newOption("default.color", ChatColor.RED));
    public static final BooleanOption VERBOSE = registerOption(VERBOSE_KEY, BooleanOption.newOption("verbose", false));
    public static final EntityTypeListOption MOBS = registerOption(MOBS_SECTION_KEY, EntityTypeListOption.newOption("mobs", Lists.newArrayList()));
    public static final StringListOption WORLDS = registerOption(EXCLUDED_WORLDS_KEY, StringListOption.newOption("excluded-worlds", Lists.newArrayList()));

    public ConfigManager(FileConfiguration config, File configFile) {
        this.config = config;
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

    public void loadConfig() {
        VERBOSE.load(config);
        for(ConfigOption opt : options.values())  {
            if(!opt.load(config)) {
                if(VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Option " + opt.getPath() + " failed to load from config");
                }
            }
        }
        MobMerge.LOG.info("Loaded " + options.values().size() + " option(s)");

        RADIUS.setValue(10);
    }

    public void saveConfig() {
        options.values().forEach( opt -> {
            opt.save(config);
        });
        try {
            config.save(configFile);
        } catch (IOException e) {
            MobMerge.LOG.severe("Error saving config file " + configFile.getAbsolutePath());
        }
    }
}

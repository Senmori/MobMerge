package net.senmori.mobmerge.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.options.ConfigOption;
import net.senmori.mobmerge.configuration.options.ConfigurationKey;
import net.senmori.mobmerge.configuration.options.types.BooleanOption;
import net.senmori.mobmerge.configuration.options.types.ChatColorOption;
import net.senmori.mobmerge.configuration.options.types.EntityTypeListOption;
import net.senmori.mobmerge.configuration.options.types.NumberOption;
import net.senmori.mobmerge.configuration.options.types.StringListOption;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager<T extends ConfigOption> {
    private static final BiMap<ConfigurationKey, ConfigOption<?>> options = HashBiMap.create();

    private final FileConfiguration config;

    // ConfigurationKeys
    public static final ConfigurationKey DEFAULT_RADIUS = ConfigurationKey.create("Default Radius", NumberOption.class);
    public static final ConfigurationKey DEFAULT_INTERVAL = ConfigurationKey.create("Default Interval", NumberOption.class);
    public static final ConfigurationKey DEFAULT_COUNT = ConfigurationKey.create("Default Count", NumberOption.class);
    public static final ConfigurationKey DEFAULT_CHAT_COLOR = ConfigurationKey.create("Default Chat Color", ChatColorOption.class);
    public static final ConfigurationKey MOBS_SECTION_KEY = ConfigurationKey.create("Mobs Section Key", EntityTypeListOption.class);
    public static final ConfigurationKey EXCLUDED_WORLDS_KEY = ConfigurationKey.create("Excluded Worlds", null);
    public static final ConfigurationKey VERBOSE_KEY = ConfigurationKey.create("Verbose", BooleanOption.class);

    // Options
    public static final NumberOption RADIUS = registerOption(DEFAULT_RADIUS, NumberOption.newOption("default.radius", 5));
    public static final NumberOption INTERVAL = registerOption(DEFAULT_INTERVAL, NumberOption.newOption("default.interval", 20));
    public static final NumberOption MAX_COUNT = registerOption(DEFAULT_COUNT, NumberOption.newOption("default.count", 256));
    public static final ChatColorOption DEFAULT_COLOR = registerOption(DEFAULT_CHAT_COLOR, ChatColorOption.newOption("default.color", ChatColor.RED));
    public static final BooleanOption VERBOSE = registerOption(VERBOSE_KEY, BooleanOption.newOption("verbose", true));
    public static final EntityTypeListOption MOBS = registerOption(MOBS_SECTION_KEY, EntityTypeListOption.newOption("mobs", Lists.newArrayList()));
    public static final StringListOption WORLDS = registerOption(EXCLUDED_WORLDS_KEY, StringListOption.newOption("excluded-worlds", Lists.newArrayList()));

    public ConfigManager(FileConfiguration config) {
        this.config = config;
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

    public static ConfigurationKey getOptionName(ConfigOption option) {
        return options.inverse().getOrDefault(option, ConfigurationKey.NULL_KEY);
    }

    public void loadConfig() {
        for(ConfigOption opt : options.values())  {
            if(!opt.load(config)) {
                if(VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Option " + opt.getPath() + " failed to load from config");
                }
            } else if(VERBOSE.getValue()) {
                MobMerge.LOG.info("Loaded {" + opt.toString() + "}");
            }
        }
    }
}

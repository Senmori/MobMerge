package net.senmori.mobmerge.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.types.BooleanOption;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
import net.senmori.mobmerge.configuration.option.types.StringListOption;
import net.senmori.mobmerge.configuration.option.types.StringOption;
import net.senmori.mobmerge.configuration.option.types.VectorOption;
import net.senmori.mobmerge.configuration.option.types.WorldListOption;
import net.senmori.mobmerge.options.EntityOptionManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static final ConfigOption EMPTY_OPTION = NumberOption.newOption(null, null);
    public static final int TICKS_PER_SECOND = 20;
    private static EntityOptionManager entityOptionManager;
    private static final BiMap<String, ConfigOption> options = HashBiMap.create();

    // class variables
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager;
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final File configFile;

    // Special keys that are useful
    public static final String MOBS_KEY = "mobs";
    public static final String CONDITIONS_KEY = "conditions";

    // Options
    public static final VectorOption RADIUS = registerOption("Default Radius", VectorOption.newOption("default.radius", new Vector(5, 5, 5)));
    public static final NumberOption INTERVAL = registerOption("Default Interval", NumberOption.newOption("default.interval", 5));
    public static final NumberOption MAX_COUNT = registerOption("Default Max Count", NumberOption.newOption("default.count", 65536));
    public static final ChatColorOption DEFAULT_COLOR = registerOption("Default Chat Color", ChatColorOption.newOption("default.color", ChatColor.RED));
    public static final BooleanOption VERBOSE = registerOption("Verbose", BooleanOption.newOption("verbose", true));
    public static final WorldListOption EXCLUDED_WORLDS = registerOption("Excluded Worlds", WorldListOption.newOption("excluded-worlds", Lists.newArrayList()));
    public static final StringListOption DEFAULT_MOBS = registerOption("Default Mobs", StringListOption.newOption("mobs.default", Lists.newArrayList()));
    public static final StringOption MERGED_ENTITY_TAG = registerOption("Default Entity Tag", StringOption.newOption("default.tag", "mergedEntity"));

    public ConfigManager(JavaPlugin plugin, File configFile) {
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.configFile = configFile;
        this.optionManager = new EntityOptionManager(this);
        entityOptionManager = this.optionManager;
        this.conditionManager = ConditionManager.getInstance();
        loadConfig();
    }


    public static <T extends ConfigOption> T registerOption(String key, T option) {
        options.put(key, option);
        return option;
    }

    public static ConfigOption<?> getOption(String key) {
        return options.get(key);
    }

    public static ConfigOption<?> getOptionByPath(String path) {
        return options.values().stream().filter(c -> c.getPath().equals(path)).limit(1).findFirst().orElse(EMPTY_OPTION);
    }

    public static String getConfigurationKey(ConfigOption option) {
        return options.inverse().getOrDefault(option, "<NULL>");
    }

    public static EntityOptionManager getEntityManager() {
        return entityOptionManager;
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

    public ConditionManager getConditionManager() {
        return conditionManager;
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

package net.senmori.mobmerge.configuration;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.options.EntityOptionManager;
import net.senmori.senlib.configuration.ConfigManager;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.ChatColorOption;
import net.senmori.senlib.configuration.option.NumberOption;
import net.senmori.senlib.configuration.option.StringOption;
import net.senmori.senlib.configuration.option.VectorOption;
import net.senmori.senlib.configuration.option.WorldListOption;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;

public class SettingsManager extends ConfigManager {

    // class variables
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager;

    // Special keys that are useful
    public static final String CONDITIONS_KEY = "conditions";

    // Options
    public final VectorOption RADIUS = registerOption("Default Radius", VectorOption.newOption("default.radius", new Vector(5, 5, 5)));
    public final NumberOption INTERVAL = registerOption("Default Interval", NumberOption.newOption("default.interval", 5));
    public final NumberOption MAX_COUNT = registerOption("Default Max Count", NumberOption.newOption("default.count", 65536));
    public final ChatColorOption DEFAULT_COLOR = registerOption("Default Chat Color", ChatColorOption.newOption("default.color", ChatColor.RED));
    public final BooleanOption VERBOSE = registerOption("Verbose", BooleanOption.newOption("verbose", true));
    public final WorldListOption EXCLUDED_WORLDS = registerOption("Excluded Worlds", WorldListOption.newOption("excluded-worlds", Lists.newArrayList()));
    public final StringOption MERGED_ENTITY_TAG = registerOption("Default Entity Tag", StringOption.newOption("default.tag", "mergedEntity"));

    public SettingsManager(JavaPlugin plugin, File configFile) {
        super(plugin, configFile);
        this.optionManager = new EntityOptionManager(this);
        this.conditionManager = ConditionManager.getInstance();
    }

    public EntityOptionManager getEntityOptionManager() {
        return optionManager;
    }

    public ConditionManager getConditionManager() {
        return conditionManager;
    }
}

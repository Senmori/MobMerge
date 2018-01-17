package net.senmori.mobmerge.configuration;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.option.ConditionSection;
import net.senmori.mobmerge.configuration.option.DefaultSection;
import net.senmori.mobmerge.configuration.option.MobsSection;
import net.senmori.mobmerge.options.EntityOptionManager;
import net.senmori.senlib.configuration.ConfigManager;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.WorldListOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SettingsManager extends ConfigManager {

    // class variables
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager;

    // Options
    public final DefaultSection DEFAULT_SECTION;
    public final MobsSection MOBS_SECTION;
    public final ConditionSection CONDITIONS_SECTION;

    public final BooleanOption VERBOSE;
    public final WorldListOption EXCLUDED_WORLDS;

    public SettingsManager(JavaPlugin plugin, File configFile) {
        super(plugin, configFile);
        this.optionManager = new EntityOptionManager(this);
        this.conditionManager = ConditionManager.getInstance();

        DEFAULT_SECTION = registerOption("Default Section", new DefaultSection("default"));
        MOBS_SECTION = registerOption("Mobs Section", new MobsSection("mobs"));
        CONDITIONS_SECTION = registerOption("Conditions Section", new ConditionSection("conditions"));

        VERBOSE = registerOption("Verbose", BooleanOption.newOption("verbose", false));
        EXCLUDED_WORLDS = registerOption("Excluded Worlds", WorldListOption.newOption("excluded-worlds", Lists.newArrayList()));

        load(getConfig());
    }

    @Override
    public boolean load(FileConfiguration configuration) {
        return getOptions().values().stream().allMatch(opt -> opt.load(configuration)) && optionManager.load(configuration);
    }

    @Override
    public boolean save(FileConfiguration configuration) {
        return getOptions().values().stream().allMatch(opt -> opt.save(configuration)) && optionManager.save(configuration);
    }

    public EntityOptionManager getEntityOptionManager() {
        return optionManager;
    }

    public ConditionManager getConditionManager() {
        return conditionManager;
    }
}

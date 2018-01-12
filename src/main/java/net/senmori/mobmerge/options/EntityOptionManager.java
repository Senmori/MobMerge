package net.senmori.mobmerge.options;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.SettingsManager;
import net.senmori.mobmerge.configuration.option.MobsSection;
import net.senmori.senlib.configuration.ConfigOption;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Map;

public final class EntityOptionManager {
    private final Map<EntityType, EntityMatcherOptions> matcherOptions = Maps.newHashMap();
    private final SettingsManager settingsManager;
    private final Map<String, ConfigOption> options = Maps.newHashMap();

    public final MobsSection MOBS_SECTION = addOption("Mobs Section", new MobsSection("mobs"));

    public EntityOptionManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public <T extends ConfigOption> T addOption(String key, T option) {
        options.put(key, option);
        return option;
    }

    public boolean load(FileConfiguration config) {
        if(!options.values().stream().allMatch(options -> options.load(config))) {
            if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                MobMerge.LOG.warning("Failed to load mob section for EntityOptionManager");
                return false; // mobs section isn't present; or it's not a section
            }
        }
        String path = MOBS_SECTION.getPath();
        ConfigurationSection section = config.getConfigurationSection(path);
        if(section == null) {
            MobMerge.LOG.info("Expected section at key \'" + path + "\'. Found null");
            return false;
        }
        for(EntityType type : MOBS_SECTION.DEFAULT_MOBS.getValue()) {
            matcherOptions.put(type, getOptionsFor(type));
        }
        for(String node : section.getKeys(false)) {
            if((section.getCurrentPath() + "." + node).equals(MOBS_SECTION.getPath())) continue; // mobs.default == mobs.default
            EntityType entityType = EntityType.fromName(node);
            if(entityType == null || !entityType.isAlive()) {
                if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                    MobMerge.LOG.warning("Invalid entity type registered: " + node);
                }
                continue; // it's not a valid entity; ignore it.
            }
            if(section.isConfigurationSection(node)) {
                MobMerge.debug("Loading options for " + node);
                EntityMatcherOptions options = getOptionsFor(entityType);
                if(!options.load(config)) {
                    if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                        MobMerge.LOG.warning("Failed to load entity matcher options for entity type: " + entityType);
                    }
                } else {
                    matcherOptions.put(entityType, options);
                }
            }
        }
        return true;
    }

    public void save(FileConfiguration config) {
        matcherOptions.values().forEach(opt -> opt.save(config));
    }

    /**
     * Get the matching {@link EntityMatcherOptions} for this given entity type.
     * @param type the entity type to search for
     * @return the matching {@link EntityMatcherOptions}, or a new instance if none exists.
     */
    public EntityMatcherOptions getOptionsFor(EntityType type) {
        return this.matcherOptions.getOrDefault(type, new EntityMatcherOptions(this.getSettingsManager(), type));
    }

    public Map<EntityType, EntityMatcherOptions> getOptions() {
        return ImmutableMap.copyOf(matcherOptions);
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}

package net.senmori.mobmerge.options;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.SettingsManager;
import net.senmori.senlib.configuration.ConfigOption;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Map;

public final class EntityOptionManager {
    private final Map<EntityType, EntityMatcherOptions> matcherOptions = Maps.newHashMap();
    private final SettingsManager settingsManager;
    private final Map<String, ConfigOption> options = Maps.newHashMap();

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
                MobMerge.LOG.warning("Failed to load options for EntityOptionManager");
                return false; // mobs section isn't present; or it's not a section
            }
        }
        String path = settingsManager.MOBS_SECTION.getPath();
        ConfigurationSection section = config.getConfigurationSection(path);
        if(section == null) {
            MobMerge.LOG.info("Expected section at key \'" + path + "\'. Found null");
            return false;
        }
        for(EntityType type : settingsManager.MOBS_SECTION.DEFAULT_MOBS.getValue()) {
            matcherOptions.put(type, getOptionsFor(type));
        }
        for(String node : section.getKeys(false)) {
            if((section.getCurrentPath() + config.options().pathSeparator() + node).equals(settingsManager.MOBS_SECTION.getPath())) continue; // mobs.default == mobs.default
            EntityType entityType = EntityType.fromName(node);
            if(entityType == null) {
                continue; // it's another section
            }
            if(!entityType.isAlive()) {
                if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                    MobMerge.LOG.warning("Invalid entity type registered: " + node);
                }
                continue; // it's not a valid entity; ignore it.
            }
            if(section.isConfigurationSection(node)) {
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

    public boolean save(FileConfiguration config) {
        matcherOptions.values().forEach(opt -> opt.save(config));
        return true;
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

    public Map<String, ConfigOption> getConfigOptions() {
        return ImmutableMap.copyOf(options);
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}

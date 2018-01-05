package net.senmori.mobmerge.options;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

public final class EntityOptionManager {
    private final Map<EntityType, EntityMatcherOptions> matcherOptions = Maps.newHashMap();
    private final ConfigManager configManager;

    public EntityOptionManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean load(FileConfiguration config) {
        String path = ConfigManager.MOBS_KEY.getName();
        ConfigurationSection section = config.getConfigurationSection(path);
        if(section == null) {
            MobMerge.LOG.info("Expected section at key \'" + path + "\'. Found null");
            return false;
        }
        List<String> defaultTypes = ConfigManager.DEFAULT_MOBS.getValue();
        for(String types : defaultTypes) {
            EntityType entityType = EntityType.fromName(types);
            if(entityType == null || !entityType.isAlive()) {
                if(ConfigManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                    MobMerge.LOG.warning("Invalid entity type registered. Expected a valid living entity. Found " + types);
                }
                continue;
            }
            EntityMatcherOptions options = matcherOptions.getOrDefault(entityType, new EntityMatcherOptions(getConfigManager(), entityType));
            matcherOptions.put(entityType, options);
        }
        for(String key : section.getKeys(false)) {
            if((section.getCurrentPath() + "." + key).equals(ConfigManager.DEFAULT_MOBS.getPath())) continue; // mobs.default == mobs.default
            EntityType entityType = EntityType.fromName(key);
            if(entityType == null || !entityType.isAlive()) {
                if(ConfigManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                    MobMerge.LOG.warning("Invalid entity type registered: " + key);
                }
                continue; // it's not a valid entity; ignore it.
            }
            if(section.isConfigurationSection(key)) {
                EntityMatcherOptions options = matcherOptions.getOrDefault(entityType, new EntityMatcherOptions(getConfigManager(), entityType));
                if(!options.load(config)) {
                    if(ConfigManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                        MobMerge.LOG.warning("Failed to load entity matcher options for entity type: " + entityType);
                    }
                } else {
                    matcherOptions.put(entityType, options);
                }
            }
        }
        return true;
    }

    /**
     * Get the matching {@link EntityMatcherOptions} for this given entity type.
     * @param type the entity type to search for
     * @return the matching {@link EntityMatcherOptions}, or a new instance if none exists.
     */
    public EntityMatcherOptions getOptionsFor(EntityType type) {
        return this.matcherOptions.getOrDefault(type, new EntityMatcherOptions(this.getConfigManager(), type));
    }

    public Map<EntityType, EntityMatcherOptions> getOptions() {
        return ImmutableMap.copyOf(matcherOptions);
    }

    public void save(FileConfiguration config) {
        matcherOptions.values().forEach(opt -> opt.save(config));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}

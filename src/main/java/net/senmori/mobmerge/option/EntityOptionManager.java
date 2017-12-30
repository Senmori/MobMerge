package net.senmori.mobmerge.option;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

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
        for(String key : section.getKeys(false)) {

            EntityType entityType = EntityType.fromName(key);
            if(entityType == null || !entityType.isAlive()) {
                MobMerge.debug("Invalid entity type registered: " + key);
                continue; // it's not a valid entity; ignore it.
            }
            if(section.isConfigurationSection(key)) {
                EntityMatcherOptions options = new EntityMatcherOptions(getConfigManager(), entityType);
                if(!options.load(config)) {
                    if(ConfigManager.VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Failed to load entity matcher options for entity type: " + entityType);
                    }
                } else {
                    matcherOptions.put(entityType, options);
                }
            }
        }
        return true;
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

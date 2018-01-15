package net.senmori.mobmerge.configuration.resolver;

import net.senmori.senlib.configuration.resolver.ObjectResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BooleanResolver extends ObjectResolver<Boolean> {
    public BooleanResolver() {
        super(Boolean.class);
    }

    @Override
    public Boolean resolve(FileConfiguration config, String path) {
        return config.isBoolean(path) ? config.getBoolean(path) : null;
    }

    @Override
    public Boolean resolve(ConfigurationSection section, String path) {
        return section.isBoolean(path) ? section.getBoolean(path) : null;
    }
}

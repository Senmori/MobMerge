package net.senmori.mobmerge.configuration.option.types;

import com.google.common.collect.Maps;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SectionOption extends StringOption {
    public SectionOption(String key, String defaultValue) {
        super(key, defaultValue);
    }

    private Map<String, ConfigOption> options = Maps.newHashMap();
    private ConfigurationSection section;

    public Map<String, ConfigOption> getOptions() {
        return options;
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public <T extends ConfigOption> T addOption(String key, T option) {
        options.put(key, option);
        return option;
    }

    @Override
    public void setValue(String key) {
        // NOOP on this type of option
    }

    @Override
    public boolean parse(String string) {
        return false; // NOOP
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        if(!config.isConfigurationSection(getPath())) return false;

        section = config.getConfigurationSection(getPath());

        AtomicBoolean error = new AtomicBoolean(false);
        for(String node : section.getKeys(false)) {
            if(!canLoadOption(node)) continue;

            getOptions().values().forEach(configOption -> {
                if(configOption.getPath().equals(node)) {
                   if(!configOption.parse(section.getString(node))) {
                       error.set(true);
                   }
                }
            });
        }
        return !error.get();
    }

    @Override
    public void save(FileConfiguration config) {
        getOptions().values().forEach(option -> config.set(getPath() + "." + option.getPath(), option.getValue()));
    }

    private boolean canLoadOption(String node) {
        return  section.isList(node) ||
                section.isSet(node) ||
                section.isConfigurationSection(node) ||
                section.get(node) instanceof ConfigurationSerializable;
    }
}

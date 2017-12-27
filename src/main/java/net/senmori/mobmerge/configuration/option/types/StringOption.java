package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.bukkit.configuration.file.FileConfiguration;

public class StringOption extends ConfigOption<String> {

    public static StringOption newOption(String key, String defaultValue) {
        return new StringOption(key, defaultValue, String.class);
    }

    protected StringOption(String key, String defaultValue, Class<String> typeClass) {
        super(key, defaultValue, typeClass);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        String str = config.getString(getPath());
        if(str != null && !str.isEmpty()) {
            setValue(str);
            return true;
        }
        return false;
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue());
    }
}
package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.bukkit.configuration.file.FileConfiguration;

public class BooleanOption extends ConfigOption<Boolean> {

    public static BooleanOption newOption(String key, boolean defaultValue) {
        return new BooleanOption(key, defaultValue);
    }

    protected BooleanOption(String key, Boolean defaultValue) {
        super(key, defaultValue, Boolean.class);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        String str = config.getString(getPath());
        if(str == null || str.isEmpty()) {
            return false;
        }
        setValue(Boolean.parseBoolean(str));
        return true;
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue());
    }
}

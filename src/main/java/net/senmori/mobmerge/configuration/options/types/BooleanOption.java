package net.senmori.mobmerge.configuration.options.types;

import net.senmori.mobmerge.configuration.options.ConfigOption;
import org.bukkit.configuration.file.FileConfiguration;

public class BooleanOption extends ConfigOption<Boolean> {

    public static BooleanOption newOption(String key, boolean defaultValue) {
        return new BooleanOption(key, defaultValue, Boolean.class);
    }

    protected BooleanOption(String key, Boolean defaultValue, Class<Boolean> typeClass) {
        super(key, defaultValue, typeClass);
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
}

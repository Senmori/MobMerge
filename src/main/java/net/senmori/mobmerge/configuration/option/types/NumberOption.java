package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;

public class NumberOption extends ConfigOption<Number> {

    public static NumberOption newOption(String key, Number defaultValue) {
        return new NumberOption(key, defaultValue);
    }

    public NumberOption(String key, Number defaultValue) {
        super(key, defaultValue, Number.class);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        return parse(config.getString(getPath()));
    }

    @Override
    public boolean parse(String strValue) {
        if(strValue != null && !strValue.isEmpty() && NumberUtils.isParsable(strValue)) {
            // it's a number
            try {
                setValue(NumberUtils.createNumber(strValue));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue());
    }
}

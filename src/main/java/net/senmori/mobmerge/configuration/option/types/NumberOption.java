package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberOption extends ConfigOption<Number> {

    public static NumberOption newOption(String key, Number defaultValue) {
        return new NumberOption(key, defaultValue, Number.class);
    }

    protected NumberOption(String key, Number defaultValue, Class<Number> typeClass) {
        super(key, defaultValue, typeClass);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        String str = config.getString(getPath());
        if(str != null && !str.isEmpty() && NumberUtils.isNumber(str)) {
            // it's a number
            try {
                setValue(NumberFormat.getInstance().parse(str));
                return true;
            } catch (ParseException e) {
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

package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Represents a config options that accepts a {@link Vector}.
 */
public class VectorOption extends ConfigOption<Vector> {

    public static VectorOption newOption(String key, int defaultRadius) {
        return new VectorOption(key, new Vector(defaultRadius, defaultRadius, defaultRadius));
    }

    public static VectorOption newOption(String key, Vector defaultVector) {
        return new VectorOption(key, defaultVector);
    }

    public VectorOption(String key, Vector defaultValue) {
        super(key, defaultValue, Vector.class);
    }

    public VectorOption(String key, Number defaultRadius) {
        this(key, new Vector(defaultRadius.doubleValue(), defaultRadius.doubleValue(), defaultRadius.doubleValue()));
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        Object obj = config.get(getPath());
        if(obj instanceof Vector) {
            setValue((Vector)obj);
        } else if(config.isList(getPath())){
            if(config.getIntegerList(getPath()) != null) {
                List<Integer> intList = config.getIntegerList(getPath());
                Vector result;
                if(intList.isEmpty()) {
                    this.currentValue = this.defaultValue; // reset value
                    return true;
                }
                switch (intList.size()) {
                    case 0:
                        result = this.defaultValue;
                        break;
                    case 1:
                        int value = intList.get(0);
                        result = new Vector(value, value, value);
                        break;
                    case 2:
                        result = new Vector(intList.get(0), intList.get(1), intList.get(0)); // use 'x' for x & z
                        break;
                    case 3:
                    default:
                        result = new Vector(intList.get(0), intList.get(1), intList.get(2));
                }
                setValue(result);
            }
        } else {
            int radius = config.getInt(getPath()); // default to a number
            setValue(new Vector(radius, radius, radius));
        }
        return true;
    }

    @Override
    public boolean parse(String string) {
        if(string != null && !string.isEmpty() && NumberUtils.isParsable(string)) {
            // it's a number
            try {
                int radius = NumberUtils.createInteger(string);
                setValue(new Vector(radius, radius, radius));
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

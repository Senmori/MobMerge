package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

/**
 * Represents a config option that accepts a {@link Vector}.
 */
public class VectorOption extends ConfigOption<Vector> {

    public static VectorOption newOption(String key, int defaultRadius) {
        return new VectorOption(key, new Vector(defaultRadius, defaultRadius, defaultRadius), Vector.class);
    }

    public static VectorOption newOption(String key, Vector defaultVector) {
        return new VectorOption(key, defaultVector, Vector.class);
    }

    protected VectorOption(String key, Vector defaultValue, Class<Vector> typeClass) {
        super(key, defaultValue, typeClass);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        Object obj = config.get(getPath());
        if(obj instanceof Vector) {
            setValue((Vector)obj);
        } else {
            int radius = config.getInt(getPath()); // default to a number
            setValue(new Vector(radius, radius, radius));
        }
        return true;
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue());
    }
}

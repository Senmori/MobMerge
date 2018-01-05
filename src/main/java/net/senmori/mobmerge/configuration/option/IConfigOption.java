package net.senmori.mobmerge.configuration.option;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * This interface represents an option in a configuration file
 * @param <T> what data type this option is
 */
public interface IConfigOption<T> {
    /**
     * The current path for this option.
     * @return the path of this option
     */
    public String getPath();

    /**
     * Gets the class that represents this option.
     * @return the class that represents this option.
     */
    public Class<T> getValueClass();

    /**
     * Gets the current value for this option.<br>
     * If the current value is null, the default value will be returned.
     * @return the current value for this option
     */
    public T getValue();

    /**
     * Set the value for this option.
     * @param value the new value
     */
    public void setValue(T value);

    /**
     * Load this option from a {@link FileConfiguration}.
     * @param config the config to load from
     * @return true if this option loaded sucessfully
     */
    public boolean load(FileConfiguration config);

    /**
     * Save this option to the passed {@link FileConfiguration}
     * @param config the config file to save to
     */
    public void save(FileConfiguration config);
}

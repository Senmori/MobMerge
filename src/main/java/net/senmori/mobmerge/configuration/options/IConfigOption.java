package net.senmori.mobmerge.configuration.options;

import org.bukkit.configuration.file.FileConfiguration;

public interface IConfigOption<T> {
    public String getPath();

    public Class<T> getValueClass();

    public T getValue();

    public void setValue(T value);

    public boolean load(FileConfiguration config);
}

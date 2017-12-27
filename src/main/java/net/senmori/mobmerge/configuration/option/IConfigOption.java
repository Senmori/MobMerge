package net.senmori.mobmerge.configuration.option;

import org.bukkit.configuration.file.FileConfiguration;

public interface IConfigOption<T> {
    public String getPath();

    public Class<T> getValueClass();

    public T getValue();

    public void setValue(T value);

    public boolean load(FileConfiguration config);

    public void save(FileConfiguration config);
}

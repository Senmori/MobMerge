package net.senmori.mobmerge.configuration.option;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigOption<T> implements IConfigOption<T> {

    protected final String key;
    protected T defaultValue;
    protected T currentValue;
    protected Class<T> typeClass;

    protected ConfigOption(String key, T defaultValue, Class<T> typeClass) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.currentValue = this.defaultValue;
        this.typeClass = typeClass;
    }

    @Override
    public String getPath() {
        return key;
    }

    @Override
    public Class<T> getValueClass() {
        return typeClass;
    }

    public T getValue() {
        return this.currentValue != null ? currentValue : defaultValue;
    }

    public void setValue(T value) {
        this.currentValue = value;
    }

    public boolean parse(String strValue) {
        T oldValue = this.currentValue;
        try {
            this.currentValue = getValueClass().cast(strValue);
        } catch(ClassCastException e) {
            this.currentValue = oldValue; // just in case
            return false;
        }
        return true;
    }

    public abstract boolean load(FileConfiguration config);

    public abstract void save(FileConfiguration config);

    public String toString() {
        return "ConfigOption={Path=" + getPath() + ", Value=" + getValue().toString() + ", Type=" + getValueClass().getName() + "}";
    }
}

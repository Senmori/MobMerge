package net.senmori.mobmerge.configuration.options;

public class ConfigurationKey<T extends ConfigOption> {
    public static final ConfigurationKey NULL_KEY = ConfigurationKey.create("null", null);


    public static <T> ConfigurationKey create(String name, Class<T> clazz) {
        return new ConfigurationKey(name, clazz);
    }

    private final String name;
    private final Class<T> clazz;
    private ConfigurationKey(String name, Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return clazz;
    }
}

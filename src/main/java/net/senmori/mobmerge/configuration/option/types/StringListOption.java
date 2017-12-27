package net.senmori.mobmerge.configuration.option.types;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StringListOption extends ListOption<String> {
    private List<String> list = Lists.newArrayList();

    public static StringListOption newOption(String key, List<String> defaultValue) {
        return new StringListOption(key, defaultValue);
    }


    protected StringListOption(String key, List<String> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public List<String> getValue() {
        return list;
    }

    @Override
    public void setList(List<String> value) {
        if(value == null || value.isEmpty()) {
            list = Lists.newArrayList(); // reset array list
            return;
        }
        list.clear();
        list.addAll(value);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        if(!(config.get(getPath()) instanceof List)) return false; // it's not a list

        List<String> list = config.getStringList(getPath());
        this.list.addAll(list);
        return !this.list.isEmpty();
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue());
    }
}

package net.senmori.mobmerge.configuration.option.types;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class StringListOption extends ListOption<String> {
    protected List<String> list = Lists.newArrayList();

    public static StringListOption newOption(String key, List<String> defaultValue) {
        return new StringListOption(key, defaultValue);
    }

    public StringListOption(String key, List<String> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public List<String> getValue() {
        return list;
    }

    @Override
    public void setValue(List list) {
        this.list.clear();
        list.forEach(obj -> {
            if(!(obj instanceof String)) {
                return;
            }
            this.list.add((String)obj);
        });
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
        this.list.addAll(list == null ? Lists.newArrayList() : list);
        return true;
    }

    @Override
    public void save(FileConfiguration config) {
        List<String> save = Lists.newArrayList();
        save.addAll(list.stream().distinct().collect(Collectors.toList())); // don't allow duplicates
        config.set(getPath(), save);
    }
}

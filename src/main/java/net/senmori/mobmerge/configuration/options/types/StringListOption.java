package net.senmori.mobmerge.configuration.options.types;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Stream;

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
        Stream stream = value.stream().filter(t -> t instanceof String);
        if(stream.count() > 0) {
            list.clear();
            stream.forEach(c -> {
                list.add((String)c);
            });
        }
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        if(!(config.get(getPath()) instanceof List)) return false; // it's not a list

        List<String> list = config.getStringList(getPath());
        this.list.addAll(list);
        return !this.list.isEmpty();
    }
}

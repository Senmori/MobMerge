package net.senmori.mobmerge.configuration.option.types;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class EntityOptions extends ListOption<EntityType> {
    private List<EntityType> types = Lists.newArrayList();
    private boolean usedWildcard = false;

    public static EntityOptions newOption(String key, List<EntityType> types) {
        return new EntityOptions(key, types);
    }

    protected EntityOptions(String key, List<EntityType> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public List<EntityType> getValue() {
        return types;
    }

    @Override
    public void setList(List<EntityType> value) {
        if(value == null || value.isEmpty()) {
            types = Lists.newArrayList(); // reset array list
            return;
        }
        types.clear();
        types.addAll(value);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        this.types.clear();
        // mobs is a section, handle it different
        List<String> types = config.getStringList(getPath());
        for(String type : types) {
            try {
                EntityType eType = EntityType.fromName(type.toLowerCase());
                if(!eType.isAlive()) {
                    continue;
                }
                this.types.add(eType);
            }catch(IllegalArgumentException e) {
                return false;
            }
        }
        return !this.types.isEmpty();
    }

    @Override
    public void save(FileConfiguration config) {
        List<String> saved = Lists.newArrayList();
        types.stream().distinct().forEach(t -> {
            saved.add(t.getName());
        });
        config.set(getPath(), saved);
    }
}
